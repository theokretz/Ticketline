package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class CustomOrderService implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderRepository orderRepository;
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;
    private final NotUserRepository notUserRepository;
    private final OrderMapper orderMapper;
    private final TransactionRepository transactionRepository;
    private final SeatMapper seatMapper;
    private final TicketRepository ticketRepository;
    private final SeatRepository seatRepository;
    private final CartService cartService;
    private final ReservationRepository reservationRepository;

    @Autowired
    public CustomOrderService(OrderRepository orderRepository, PerformanceRepository performanceRepository, UserRepository userRepository,
                              NotUserRepository notUserRepository, OrderMapper orderMapper, TransactionRepository transactionRepository,
                              SeatMapper seatMapper, TicketRepository ticketRepository, SeatRepository seatRepository, CartService cartService, ReservationRepository reservationRepository) {
        this.orderRepository = orderRepository;
        this.performanceRepository = performanceRepository;
        this.userRepository = userRepository;
        this.notUserRepository = notUserRepository;
        this.orderMapper = orderMapper;
        this.transactionRepository = transactionRepository;
        this.seatMapper = seatMapper;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
        this.cartService = cartService;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    @Override
    public OrderDto buyTickets(Integer userId) throws ConflictException {
        LOGGER.debug("Buy Tickets from Cart, userId: {}", userId);

        // ApplicationUser user = userRepository.findUserByEmail(userDto.getEmail());
        // geht nicht weil ich in Test den user mit notUserRepository speichere
        // wenn login implementiert dann UserRepository zu interface machen


        ApplicationUser user = notUserRepository.findApplicationUserById(userId);
        if (user == null) {
            throw new NotFoundException("Could not find User");
        }


        //create order
        Order order = new Order();
        order.setUser(user);
        order.setCancelled(false);
        order.setOrderTs(LocalDateTime.now());
        if (!user.getLocations().iterator().hasNext()) {
            throw new FatalException("No Location is saved for this User");
        }
        order.setDeliveryAddress(user.getLocations().iterator().next());

        if (!user.getPaymentDetails().iterator().hasNext()) {
            throw new FatalException("No PaymentDetails are saved for this User");
        }
        order.setPaymentDetail(user.getPaymentDetails().iterator().next());

        orderRepository.save(order);
        List<String> conflictMsg = new ArrayList<>();
        List<CartTicketDto> ticketDtoList = cartService.getCart(userId);
        if (ticketDtoList.isEmpty()) {
            conflictMsg.add("No Tickets in Cart");
            throw new ConflictException("Cart is empty", conflictMsg);
        }

        Set<Ticket> tickets = new HashSet<>();

        BigDecimal price = new BigDecimal(0);
        //Tickets
        for (CartTicketDto cartTicketDto : ticketDtoList) {
            Ticket ticket = ticketRepository.findTicketById(cartTicketDto.getId());
            if (ticket == null) {
                orderRepository.delete(order);
                throw new NotFoundException("Could not find Ticket");
            }
            if (ticket.getOrder() != null) {
                orderRepository.delete(order);
                conflictMsg.add("Ticket " + ticket.getId() + " cannot be bought");
                throw new ConflictException("Ticket is already bought", conflictMsg);
            }
            if (!ticket.getReservation().getCart()) {
                orderRepository.delete(order);
                conflictMsg.add("Ticket " + ticket.getId() + " cannot be bought");
                throw new ConflictException("Ticket is not in cart", conflictMsg);
            }
            if (!Objects.equals(ticket.getReservation().getUser().getId(), userId)) {
                orderRepository.delete(order);
                conflictMsg.add("Ticket " + ticket.getId() + " cannot be bought");
                throw new ConflictException("Ticket is not assigned to user", conflictMsg);
            }
            price = price.add(cartTicketDto.getPrice());
            ticket.setOrder(order);
            tickets.add(ticket);
            ticket.setReservation(null);
            reservationRepository.delete(ticket.getReservation());
        }

        order.setTickets(tickets);


        //transaction
        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setDeductedAmount(price);
        Set<Transaction> transactionSet = new HashSet<>();
        if (order.getTransactions() != null) {
            transactionSet = order.getTransactions();
        }
        //TODO: deducted points?
        transaction.setDeductedPoints(0);
        transactionSet.add(transaction);


        order.setTransactions(transactionSet);


        //Saving Order in PaymentDetail
        PaymentDetail paymentDetail = user.getPaymentDetails().iterator().next();
        Set<Order> orders = new HashSet<>();
        if (paymentDetail.getOrders() != null) {
            orders = paymentDetail.getOrders();
        }
        orders.add(order);
        paymentDetail.setOrders(orders);


        transactionRepository.save(transaction);

        return orderMapper.orderToOrderDto(order);
    }
}
