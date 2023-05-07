package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.PerformanceRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class PerformanceServiceImpl implements PerformanceService {

    //TODO: constutor injection
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private PerformanceRepository performanceRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotUserRepository notUserRepository;

    @Transactional
    @Override
    public Order buyTickets(CartDto cartDto, int performanceId, UserDto userDto) {
        //TODO: validation

        //   ApplicationUser user = userRepository.findUserByEmail(userDto.getEmail());

        Optional<ApplicationUser> optonalUser = notUserRepository.findById(userDto.getId());
        ApplicationUser user = optonalUser.get();

        Set<Ticket> tickets = new HashSet<>();
        Order order = new Order();
        order.setUser(user);
        order.setCancelled(false);
        order.setOrderTs(LocalDateTime.now());

        for (CartSeatDto cartSeatDto : cartDto.getSeats()) {
            Ticket ticket = new Ticket();
            Optional<Performance> performanceOptional = performanceRepository.findById(performanceId);
            if (performanceOptional.isEmpty()) {
                //TODO: exception

            }
            ticket.setPerformance(performanceOptional.get());
            Seat seat = new Seat();
            seat.setNumber(cartSeatDto.getNumber());
            seat.setRow(cartSeatDto.getRow());
            ticket.setSeat(seat);
            ticket.setOrder(order);
            tickets.add(ticket);
        }

        for (int i = 0; i < cartDto.getStanding(); i++) {
            Ticket ticket = new Ticket();
            ticket.setOrder(order);
            tickets.add(ticket);
        }
        BigDecimal price = new BigDecimal(0);
        //TODO: add prices

        order.setTickets(tickets);


        //transaction
        Transaction transaction = new Transaction();
        transaction.setOrder(order);
        transaction.setDeductedAmount(price);
        Set<Transaction> transactionSet = new HashSet<>();
        transactionSet.add(transaction);

        order.setTransactions(transactionSet);
        order.setDeliveryAdress(userDto.getLocations().iterator().next());
        order.setPaymentDetail(userDto.getPaymentDetails().iterator().next());

        //Saving Order in PaymentDetail
        PaymentDetail paymentDetail = user.getPaymentDetails().iterator().next();
        Set<Order> orders = new HashSet<>();
        orders.add(order);
        paymentDetail.setOrders(orders);


        //TODO: save order
        orderRepository.save(order);
        return order;
    }
}
