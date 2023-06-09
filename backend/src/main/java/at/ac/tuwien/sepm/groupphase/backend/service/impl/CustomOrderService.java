package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderHistoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.UnauthorizedException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseOrderedRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.StrictMath.floor;

@Service
public class CustomOrderService implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderRepository orderRepository;
    private final NotUserRepository notUserRepository;
    private final OrderMapper orderMapper;
    private final TransactionRepository transactionRepository;
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    private final MerchandiseRepository merchandiseRepository;
    private final MerchandiseOrderedRepository merchandiseOrderedRepository;
    private final CustomOrderValidator validator;

    @Autowired
    public CustomOrderService(OrderRepository orderRepository, NotUserRepository notUserRepository,
                              OrderMapper orderMapper, TransactionRepository transactionRepository,
                              TicketRepository ticketRepository, ReservationRepository reservationRepository,
                              MerchandiseRepository merchandiseRepository,
                              MerchandiseOrderedRepository merchandiseOrderedRepository,
                              CustomOrderValidator validator) {
        this.orderRepository = orderRepository;
        this.notUserRepository = notUserRepository;
        this.orderMapper = orderMapper;
        this.transactionRepository = transactionRepository;
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.merchandiseRepository = merchandiseRepository;
        this.merchandiseOrderedRepository = merchandiseOrderedRepository;
        this.validator = validator;
    }


    @Override
    public List<OrderHistoryDto> getOrderHistory(Integer id) throws NotFoundException, ValidationException {
        LOGGER.info("Find all orders for user with id {}", id);
        List<String> validationErrors = new ArrayList<>();
        if (id == null) {
            throw new NotFoundException(String.format("No id has been provided"));
        }
        if (id <= 0) {
            validationErrors.add("Id can't be a negative number");
        }
        if (!validationErrors.isEmpty()) {
            throw new ValidationException(String.format("Validation failed:"), validationErrors);
        }
        ApplicationUser user = notUserRepository.getApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException(String.format("Could not find user with id %s", id));
        }
        List<Order> allOrders = orderRepository.getAllOrdersByUserId(id);
        List<OrderHistoryDto> allOrdersDto = new ArrayList<>();
        for (Order order : allOrders) {
            allOrdersDto.add(orderMapper.orderToOrderHistoryDto(order));
        }
        return allOrdersDto;
    }

    @Transactional(rollbackFor = { ConflictException.class, ValidationException.class })
    @Override
    public OrderDto buyTickets(Integer userId, BookingDto bookingDto) throws ConflictException, ValidationException {
        LOGGER.debug("Buy Tickets from Cart, userId: {}", userId);
        List<String> conflictMsg = new ArrayList<>();
        List<String> validationMsg = new ArrayList<>();

        // input validation
        boolean hasTickets = bookingDto.getTickets() != null && !bookingDto.getTickets().isEmpty();
        boolean hasMerchandise = bookingDto.getMerchandise() != null && !bookingDto.getMerchandise().isEmpty();
        boolean buysTickets = false;
        if (hasTickets) {
            buysTickets = bookingDto.getTickets().stream().anyMatch(ticketDto -> !ticketDto.getReservation());
        }
        boolean orderIsCreated = buysTickets || hasMerchandise;
        // throw exception if bookingDto both tickets and merchandise are null
        if (!hasTickets && !hasMerchandise) {
            validationMsg.add("No Tickets or Merchandise in Booking");
        }
        // throw exception if tickets or merchandise are bought and no payment id given
        if (orderIsCreated && bookingDto.getPaymentDetailId() == null) {
            validationMsg.add("No payment id for buying tickets");
        }
        // throw exception if bookingDto has merchandise and no location id
        if (hasMerchandise && bookingDto.getLocationId() == null) {
            validationMsg.add("No delivery address for merchandise");
        }
        if (!validationMsg.isEmpty()) {
            throw new ValidationException("Error processing order", validationMsg);
        }

        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }
        validator.validPoints(user.getPoints(), bookingDto);

        //create order
        Order order = null;
        if (orderIsCreated) {
            order = new Order();
            order.setUser(user);
            order.setCancelled(false);
            order.setOrderTs(LocalDateTime.now());

            // check if bookingDto.getPaymentDetailId() is contained in user.getPaymentDetails()
            if (bookingDto.getPaymentDetailId() != null) {
                PaymentDetail paymentDetail =
                    user.getPaymentDetails().stream().filter(paymentDetail1 -> paymentDetail1.getId().equals(bookingDto.getPaymentDetailId())).findFirst()
                        .orElse(null);
                if (paymentDetail == null) {
                    conflictMsg.add("Payment Detail not found for User");
                    throw new ConflictException("Error creating order", conflictMsg);
                }

                Set<Order> orders = new HashSet<>();
                if (paymentDetail.getOrders() != null) {
                    orders = paymentDetail.getOrders();
                }
                orders.add(order);
                paymentDetail.setOrders(orders);
                order.setPaymentDetail(paymentDetail);
            }
            // check if bookingDto.getLocationId() is contained in user.getLocations()
            if (bookingDto.getLocationId() != null) {
                Location location =
                    user.getLocations().stream().filter(location1 -> location1.getId().equals(bookingDto.getLocationId())).findFirst().orElse(null);
                if (location == null) {
                    conflictMsg.add("Location not found for User");
                    throw new ConflictException("Error creating order", conflictMsg);
                }
                order.setDeliveryAddress(location);
            }
            orderRepository.save(order);
        }

        BigDecimal price = new BigDecimal(0);
        int totalPoints = 0;
        // Tickets
        if (hasTickets) {
            // fetch tickets from db
            List<Ticket> fetchedTickets = ticketRepository.findAllByIdIn(
                bookingDto.getTickets().stream().map(BookingTicketDto::getTicketId).collect(Collectors.toList()));

            // check if all tickets are found
            if (fetchedTickets.size() != bookingDto.getTickets().size()) {
                conflictMsg.add("Not all tickets found");
                throw new ConflictException("Error creating order", conflictMsg);
            }

            Set<Ticket> ticketsToSave = new HashSet<>();
            Set<Reservation> reservationsToSave = new HashSet<>();
            Set<Reservation> reservationsToDelete = new HashSet<>();

            for (Ticket ticket : fetchedTickets) {

                // find ticket in bookingDto by id
                BookingTicketDto bookingTicketDto =
                    bookingDto.getTickets().stream().filter(ticketDto -> ticketDto.getTicketId().equals(ticket.getId())).findFirst().get();

                // check if ticket is already bought or reserved
                if (ticket.getOrder() != null
                    || (ticket.getReservation() != null
                    && (!ticket.getReservation().getUser().getId().equals(user.getId())
                    || (bookingTicketDto.getReservation() && !ticket.getReservation().getCart())))) {
                    conflictMsg.add("Ticket " + ticket.getId() + " is already bought or reserved");
                    continue;
                }
                if (bookingTicketDto.getReservation()) {
                    // reserve ticket
                    Integer id = ticket.getReservation() == null ? null : ticket.getReservation().getId();
                    Reservation reservation = Reservation.ReservationBuilder.aReservation()
                        .withId(id)
                        .withTicket(ticket)
                        .withCart(false)
                        .withExpirationTs(ticket.getPerformance().getDatetime().minusMinutes(30))
                        .withUser(user)
                        .build();
                    reservationsToSave.add(reservation);
                } else {
                    // buy ticket
                    BigDecimal ticketPrice = getTicketPrice(ticket);
                    price = price.add(ticketPrice);
                    totalPoints += (int) floor(ticketPrice.doubleValue());
                    ticket.setOrder(order);
                    ticketsToSave.add(ticket);
                    if (ticket.getReservation() != null) {
                        reservationsToDelete.add(ticket.getReservation());
                    }
                }
            }
            if (!conflictMsg.isEmpty()) {
                throw new ConflictException("Error creating order", conflictMsg);
            }

            reservationRepository.saveAll(reservationsToSave);
            reservationRepository.deleteAll(reservationsToDelete);
            if (orderIsCreated) {
                order.setTickets(ticketsToSave);
            }
        }

        //Merchandise
        if (hasMerchandise) {
            List<Merchandise> fetchedMerchandise = merchandiseRepository.findAllById(
                bookingDto.getMerchandise().stream().map(BookingMerchandiseDto::getId).collect(Collectors.toList()));

            if (fetchedMerchandise.size() != bookingDto.getMerchandise().size()) {
                conflictMsg.add("Not all merchandise found");
                throw new ConflictException("Error creating order", conflictMsg);
            }

            Set<MerchandiseOrdered> merchandiseOrderedToSave = new HashSet<>();

            for (BookingMerchandiseDto merchandiseDto : bookingDto.getMerchandise()) {
                Merchandise merchandise =
                    fetchedMerchandise.stream().filter(merchandise1 -> merchandise1.getId().equals(merchandiseDto.getId())).findFirst().get();
                MerchandiseOrdered merchandiseOrdered = MerchandiseOrdered.MerchandiseOrderedBuilder.aMerchandiseOrdered()
                    .withMerchandise(merchandise)
                    .withOrder(order)
                    .withQuantity(merchandiseDto.getQuantity())
                    .withPoints(merchandiseDto.getBuyWithPoints())
                    .build();
                merchandiseOrderedToSave.add(merchandiseOrdered);
                if (!merchandiseDto.getBuyWithPoints()) {
                    BigDecimal merchPrice = merchandise.getPrice().multiply(new BigDecimal(merchandiseDto.getQuantity()));
                    price = price.add(merchPrice);
                    totalPoints += (int) floor(merchPrice.doubleValue());
                } else {
                    totalPoints -= merchandise.getPointsPrice() * merchandiseDto.getQuantity();
                }
            }

            merchandiseOrderedRepository.saveAll(merchandiseOrderedToSave);
            order.setMerchandiseOrdered(merchandiseOrderedToSave);
        }

        //transaction
        if (orderIsCreated) {
            Transaction transaction = new Transaction();
            transaction.setOrder(order);
            transaction.setDeductedAmount(price);
            transaction.setDeductedPoints(totalPoints);
            transaction.setTransactionTs(LocalDateTime.now());
            order.setTransactions(Collections.singleton(transaction));
            user.setPoints(user.getPoints() + totalPoints);
            transactionRepository.save(transaction);
        }

        return orderMapper.orderToOrderDto(order);
    }

    @Transactional(rollbackFor = { UnauthorizedException.class, ConflictException.class, ValidationException.class })
    @Override
    public void cancelItems(Integer userId, Integer orderId, Integer[] tickets, Integer[] merchandise) throws UnauthorizedException, ConflictException, ValidationException {
        LOGGER.debug("cancelItems({}, {}, {}, {})", userId, orderId, tickets, merchandise);

        if (tickets.length == 0 && merchandise.length == 0) {
            throw new ValidationException("Error cancelling order", List.of("No items to cancel"));
        }

        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        Order order = getAndCheckOrder(userId, orderId);

        List<String> conflictMsg = new ArrayList<>();
        for (Integer ticket : tickets) {
            // check if ticket.getId() is in order.getTickets()
            Optional<Ticket> ticketToCancel = order.getTickets().stream().filter(t -> t.getId().equals(ticket)).findFirst();
            if (ticketToCancel.isEmpty()) {
                conflictMsg.add("Ticket " + ticket + " is not in order");
                continue;
            }
            // check if ticket performance is in the past
            if (ticketToCancel.get().getPerformance().getDatetime().isBefore(LocalDateTime.now())) {
                conflictMsg.add("Ticket " + ticket + " is in the past");
            }
        }
        for (Integer merch : merchandise) {
            // check if merchandise.getId() is in order.getMerchandiseOrdered()
            Optional<MerchandiseOrdered> merchandiseToCancel = order.getMerchandiseOrdered().stream().filter(m -> m.getId().equals(merch)).findFirst();
            if (merchandiseToCancel.isEmpty()) {
                conflictMsg.add("Merchandise " + merch + " is not in order");
                continue;
            }
            // check if order ts is more than 3 days ago
            if (order.getOrderTs().isBefore(LocalDateTime.now().minusDays(3))) {
                conflictMsg.add("Merchandise " + merch + " has already been shipped");
            }
        }
        if (!conflictMsg.isEmpty()) {
            throw new ConflictException("Error cancelling order", conflictMsg);
        }

        BigDecimal price = new BigDecimal(0);
        Integer totalPoints = 0;
        Transaction transaction = new Transaction();
        transaction.setOrder(order);

        for (Integer ticket : tickets) {
            // find ticket in order.getTickets()
            Ticket ticketToCancel = order.getTickets().stream().filter(t -> t.getId().equals(ticket)).findFirst().get();
            BigDecimal ticketPrice = getTicketPrice(ticketToCancel);
            price = price.subtract(ticketPrice);
            totalPoints -= (int) floor(ticketPrice.doubleValue());
            ticketToCancel.setOrder(null);
        }

        for (Integer merch : merchandise) {
            // find merchandise in order.getMerchandiseOrdered()
            MerchandiseOrdered merchandiseOrderedToCancel = order.getMerchandiseOrdered().stream().filter(m -> m.getId().equals(merch)).findFirst().get();
            if (!merchandiseOrderedToCancel.getPoints()) {
                BigDecimal merchPrice = merchandiseOrderedToCancel.getMerchandise().getPrice().multiply(new BigDecimal(merchandiseOrderedToCancel.getQuantity()));
                price = price.subtract(merchPrice);
                int points = (int) floor(merchPrice.doubleValue());
                totalPoints -= points;
            } else {
                totalPoints += merchandiseOrderedToCancel.getMerchandise().getPointsPrice() * merchandiseOrderedToCancel.getQuantity();
            }
            merchandiseOrderedRepository.delete(merchandiseOrderedToCancel);
        }

        transaction.setDeductedAmount(price);
        transaction.setDeductedPoints(totalPoints);
        transaction.setTransactionTs(LocalDateTime.now());
        transactionRepository.save(transaction);
        Integer newPoints = user.getPoints() + totalPoints;
        if (newPoints < 0) {
            throw new ConflictException("Error cancelling order", List.of("User has already spent points"));
        }
        user.setPoints(newPoints);
        if (tickets.length == order.getTickets().size() && merchandise.length == order.getMerchandiseOrdered().size()) {
            order.setCancelled(true);
        }
    }

    @Transactional(rollbackFor = { UnauthorizedException.class, ConflictException.class, ValidationException.class })
    @Override
    public void cancelOrder(Integer userId, Integer orderId) throws UnauthorizedException, ConflictException {
        LOGGER.debug("cancelOrder({}, {})", userId, orderId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        Order order = getAndCheckOrder(userId, orderId);

        if (!order.getTickets().isEmpty() && order.getTickets().stream().anyMatch(t -> t.getPerformance().getDatetime().isBefore(LocalDateTime.now()))) {
            throw new ConflictException("Error cancelling order", List.of("Ticket has already been used"));
        }

        if (!order.getMerchandiseOrdered().isEmpty() && order.getOrderTs().isBefore(LocalDateTime.now().minusDays(3))) {
            throw new ConflictException("Error cancelling order", List.of("Order has already been shipped"));
        }

        BigDecimal price = new BigDecimal(0);
        Integer totalPoints = 0;
        Transaction transaction = new Transaction();
        transaction.setOrder(order);

        for (Ticket ticket : order.getTickets()) {
            BigDecimal ticketPrice = getTicketPrice(ticket);
            price = price.subtract(ticketPrice);
            totalPoints -= (int) floor(ticketPrice.doubleValue());
            ticket.setOrder(null);
        }

        for (MerchandiseOrdered merchandiseOrdered : order.getMerchandiseOrdered()) {
            if (!merchandiseOrdered.getPoints()) {
                BigDecimal merchPrice = merchandiseOrdered.getMerchandise().getPrice().multiply(new BigDecimal(merchandiseOrdered.getQuantity()));
                price = price.subtract(merchPrice);
                totalPoints -= (int) floor(merchPrice.doubleValue());
            } else {
                totalPoints += merchandiseOrdered.getMerchandise().getPointsPrice() * merchandiseOrdered.getQuantity();
            }
            merchandiseOrderedRepository.delete(merchandiseOrdered);
        }

        transaction.setDeductedAmount(price);
        transaction.setDeductedPoints(totalPoints);
        transaction.setTransactionTs(LocalDateTime.now());
        Integer newPoints = user.getPoints() + totalPoints;
        if (newPoints < 0) {
            throw new ConflictException("Error cancelling order", List.of("User has already spent points"));
        }
        transactionRepository.save(transaction);
        user.setPoints(newPoints);
        order.setCancelled(true);
    }


    private Order getAndCheckOrder(Integer userId, Integer orderId) throws UnauthorizedException, ConflictException {
        Order order = orderRepository.getOrderHereById(orderId);
        if (order == null) {
            throw new NotFoundException("Could not find Order");
        }

        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Error cancelling order", List.of("User is not authorized to cancel this order"));
        }

        if (order.getCancelled()) {
            throw new ConflictException("Error cancelling order", List.of("Order is already cancelled"));
        }
        return order;
    }

    private BigDecimal getTicketPrice(Ticket ticket) {
        Optional<PerformanceSector> matchingSector = ticket.getPerformance().getPerformanceSectors()
            .stream()
            .filter(perfSector -> perfSector.getSector() == ticket.getSeat().getSector())
            .findFirst();

        if (matchingSector.isPresent()) {
            return matchingSector.get().getPrice();
        } else {
            throw new FatalException("No Performance Sector assigned");
        }
    }


    @Override
    public Order getOrder(Integer userId, Integer orderId) throws UnauthorizedException {
        LOGGER.debug("cancelOrder({}, {})", userId, orderId);
        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }

        Order order = orderRepository.getOrderNowById(orderId);
        if (order == null) {
            throw new NotFoundException("Could not find Order");
        }

        if (!order.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Error cancelling order", List.of("User is not authorized to cancel this order"));
        }

        return order;
    }
}
