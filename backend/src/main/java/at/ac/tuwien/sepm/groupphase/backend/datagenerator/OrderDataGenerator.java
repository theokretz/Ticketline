package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Profile("generateData")
@Component
@DependsOn({"userDataGenerator", "paymentDetailDataGenerator", "locationDataGenerator", "ticketDataGenerator"})
public class OrderDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final NotUserRepository notUserRepository;

    private final OrderRepository orderRepository;

    private final TicketRepository ticketRepository;


    public OrderDataGenerator(NotUserRepository notUserRepository, OrderRepository orderRepository, TicketRepository ticketRepository) {
        this.notUserRepository = notUserRepository;
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    @PostConstruct
    @Transactional
    private void generateOrder() {
        if (orderRepository.findAll().size() > 0) {
            LOGGER.debug("order already generated");
        } else {
            LOGGER.debug("generating order entries");
            List<Ticket> tickets = ticketRepository.findAllByOrderIsNullAndReservationIsNull();
            notUserRepository.findAllWithLocationsAndPaymentDetails().forEach(user -> {


                Set<Ticket> boughtTicket = new HashSet<>();

                Iterator<Ticket> ticketIter = tickets.iterator();
                while (boughtTicket.size() < 4 && ticketIter.hasNext()) {
                    boughtTicket.add(ticketIter.next());
                }

                if (boughtTicket.isEmpty()) {
                    return;
                }
                Order order = Order.OrderBuilder.aOrder()
                    .setOrderTs(LocalDateTime.now())
                    .setCancelled(false)
                    .setUser(user)
                    .setDeliveryAdress(user.getLocations().iterator().next())
                    .setPaymentDetail(user.getPaymentDetails().iterator().next())
                    .setTickets(boughtTicket)
                    .build();
                LOGGER.debug("saving order {}", order);
                order = orderRepository.save(order);
                tickets.removeAll(boughtTicket);
                for (Ticket t : boughtTicket) {
                    t.setOrder(order);
                }
                ticketRepository.saveAll(boughtTicket);
            });
        }
    }
}