package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    //TODO: constutor injection
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderRepository orderRepository;
    private final PerformanceRepository performanceRepository;
    private final UserRepository userRepository;
    private final NotUserRepository notUserRepository;
    private final OrderMapper orderMapper;

    public PerformanceServiceImpl(OrderRepository orderRepository, PerformanceRepository performanceRepository, UserRepository userRepository, NotUserRepository notUserRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.performanceRepository = performanceRepository;
        this.userRepository = userRepository;
        this.notUserRepository = notUserRepository;
        this.orderMapper = orderMapper;
    }

    @Transactional
    @Override
    public OrderDto buyTickets(CartDto cartDto, int performanceId, UserDto userDto) {
        //TODO: validation

        // ApplicationUser user = userRepository.findUserByEmail(userDto.getEmail());
        // geht nicht weil ich in Test den user mit notUserRepository speichere
        // wenn login implementiert dann UserRepository zu interface machen

        Optional<ApplicationUser> optonalUser = notUserRepository.findById(userDto.getId());
        if (optonalUser.isEmpty()) {
            throw new FatalException("User cannot be null");
        }
        ApplicationUser user = optonalUser.get();

        Set<Ticket> tickets = new HashSet<>();
        Order order = new Order();
        order.setUser(user);
        order.setCancelled(false);
        order.setOrderTs(LocalDateTime.now());

        //Seats
        for (CartSeatDto cartSeatDto : cartDto.getSeats()) {
            Ticket ticket = new Ticket();
            Optional<Performance> performanceOptional = performanceRepository.findById(performanceId);
            if (performanceOptional.isEmpty()) {
                throw new FatalException("Performance cannot be null");
            }
            ticket.setPerformance(performanceOptional.get());
            Seat seat = new Seat();
            seat.setNumber(cartSeatDto.getNumber());
            seat.setRow(cartSeatDto.getRow());
            ticket.setSeat(seat);
            ticket.setOrder(order);
            tickets.add(ticket);
        }

        //Standing
        for (int i = 0; i < cartDto.getStanding(); i++) {
            Ticket ticket = new Ticket();
            ticket.setOrder(order);
            tickets.add(ticket);
            Optional<Performance> performanceOptional = performanceRepository.findById(performanceId);
            if (performanceOptional.isEmpty()) {
                throw new FatalException("Performance cannot be null");
            }
            ticket.setPerformance(performanceOptional.get());
        }

        BigDecimal price = new BigDecimal(0);
        //TODO: add prices

        order.setTickets(tickets);


        //transaction
        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setDeductedAmount(price);
        Set<Transaction> transactionSet = new HashSet<>();
        if (order.getTransactions() != null) {
            transactionSet = order.getTransactions();
        }
        transactionSet.add(transaction);

        order.setTransactions(transactionSet);
        order.setDeliveryAdress(userDto.getLocations().iterator().next());
        order.setPaymentDetail(userDto.getPaymentDetails().iterator().next());

        //Saving Order in PaymentDetail
        PaymentDetail paymentDetail = user.getPaymentDetails().iterator().next();
        Set<Order> orders = new HashSet<>();
        if (paymentDetail.getOrders() != null) {
            orders = paymentDetail.getOrders();
        }
        orders.add(order);
        paymentDetail.setOrders(orders);


        orderRepository.save(order);
        return orderMapper.orderToDto(order);
    }
}
