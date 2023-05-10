package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.OrderMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SeatMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.exception.DtoException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TransactionRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PerformanceServiceImpl(OrderRepository orderRepository, PerformanceRepository performanceRepository, UserRepository userRepository,
                                  NotUserRepository notUserRepository, OrderMapper orderMapper, TransactionRepository transactionRepository,
                                  SeatMapper seatMapper, TicketRepository ticketRepository, SeatRepository seatRepository) {
        this.orderRepository = orderRepository;
        this.performanceRepository = performanceRepository;
        this.userRepository = userRepository;
        this.notUserRepository = notUserRepository;
        this.orderMapper = orderMapper;
        this.transactionRepository = transactionRepository;
        this.seatMapper = seatMapper;
        this.ticketRepository = ticketRepository;
        this.seatRepository = seatRepository;
    }

    @Transactional
    @Override
    public OrderDto buyTickets(CartDto cartDto, int performanceId, UserDto userDto) {
        LOGGER.debug("Buy Tickets from Cart {}, performanceId: {}, UserDto: {}", cartDto, performanceId, userDto);
        // ApplicationUser user = userRepository.findUserByEmail(userDto.getEmail());
        // geht nicht weil ich in Test den user mit notUserRepository speichere
        // wenn login implementiert dann UserRepository zu interface machen

        if (cartDto == null) {
            throw new DtoException("Cart cannot be null");
        }
        if (userDto == null) {
            throw new DtoException("User cannot be null");
        }

        Optional<ApplicationUser> optionalApplicationUser = notUserRepository.findById(userDto.getId());
        if (optionalApplicationUser.isEmpty()) {
            throw new FatalException("Could not find User");
        }
        ApplicationUser user = optionalApplicationUser.get();

        //performance
        Performance performance = performanceRepository.findPerformanceById(performanceId);
        if (performance == null) {
            throw new FatalException("Could not find Performance");
        }

        //create order
        Order order = new Order();
        order.setUser(user);
        order.setCancelled(false);
        order.setOrderTs(LocalDateTime.now());
        order.setDeliveryAdress(userDto.getLocations().iterator().next());
        order.setPaymentDetail(userDto.getPaymentDetails().iterator().next());
        orderRepository.save(order);

        Set<Ticket> tickets = new HashSet<>();
        //Tickets
        for (CartSeatDto cartSeatDto : cartDto.getSeats()) {
            Ticket ticket = ticketRepository.findTicketBySeatId(cartSeatDto.getId());
            if (ticket == null) {
                throw new FatalException("Could not find Ticket");
            }

            ticket.setOrder(order);
            tickets.add(ticket);
        }

        //Price
        BigDecimal price = new BigDecimal(0);
        boolean help = false;
        for (Ticket ticket : tickets) {
            Sector sector = ticket.getSeat().getSector();
            Set<PerformanceSector> performanceSectors = sector.getPerformanceSectors();
            for (PerformanceSector perfSector : performanceSectors) {
                if (perfSector.getPerformance().getId() == performanceId && !help && perfSector.getSector() == sector) {
                    price = price.add(perfSector.getPrice());
                    help = true;
                }
            }
            help = false;
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
        return orderMapper.orderToDto(order);
    }
}
