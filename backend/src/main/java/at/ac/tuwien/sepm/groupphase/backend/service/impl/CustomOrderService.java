package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleOrderDto;
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

    @Autowired
    public CustomOrderService(OrderRepository orderRepository, NotUserRepository notUserRepository, OrderMapper orderMapper,
                              TransactionRepository transactionRepository, TicketRepository ticketRepository, ReservationRepository reservationRepository,
                              MerchandiseRepository merchandiseRepository, MerchandiseOrderedRepository merchandiseOrderedRepository) {
        this.orderRepository = orderRepository;
        this.notUserRepository = notUserRepository;
        this.orderMapper = orderMapper;
        this.transactionRepository = transactionRepository;
        this.ticketRepository = ticketRepository;
        this.reservationRepository = reservationRepository;
        this.merchandiseRepository = merchandiseRepository;
        this.merchandiseOrderedRepository = merchandiseOrderedRepository;
    }


    @Override
    public List<SimpleOrderDto> getOrderHistory(Integer id) {
        LOGGER.info("Find all orders for user with id {}", id);
        ApplicationUser user = notUserRepository.getApplicationUserById(id);
        if (user == null) {
            throw new NotFoundException(String.format("Could not find user with id %s", id));
        }
        List<Order> allOrders = orderRepository.getAllOrdersByUserId(id);
        List<SimpleOrderDto> allOrdersDto = new ArrayList<>();
        for (Order order : allOrders) {
            allOrdersDto.add(orderMapper.orderToSimpleOrderDto(order));
        }
        return allOrdersDto;
    }

    @Transactional
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
                    // but ticket
                    Optional<PerformanceSector> matchingSector = ticket.getPerformance().getPerformanceSectors()
                        .stream()
                        .filter(perfSector -> perfSector.getSector() == ticket.getSeat().getSector())
                        .findFirst();

                    if (matchingSector.isPresent()) {
                        price = price.add(matchingSector.get().getPrice());
                    } else {
                        throw new FatalException("No Performance Sector assigned");
                    }

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
                    .withPoints(false)
                    .build();
                merchandiseOrderedToSave.add(merchandiseOrdered);
                price = price.add(merchandise.getPrice().multiply(new BigDecimal(merchandiseDto.getQuantity())));
            }

            merchandiseOrderedRepository.saveAll(merchandiseOrderedToSave);
            order.setMerchandiseOrdered(merchandiseOrderedToSave);
        }

        //transaction
        if (orderIsCreated) {
            Transaction transaction = new Transaction();
            transaction.setOrder(order);
            transaction.setDeductedAmount(price);
            //TODO: deducted points?
            transaction.setDeductedPoints(0);
            order.setTransactions(Collections.singleton(transaction));

            transactionRepository.save(transaction);
        }

        return orderMapper.orderToOrderDto(order);
    }
}
