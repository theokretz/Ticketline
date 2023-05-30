package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

    /**
     * Find all orders by user id.
     * Furthermore, the following attributes are loaded eagerly: tickets, tickets.performance,
     * tickets.seat, tickets.performance.event, tickets.performance.hall,
     * tickets.performance.event.artists, tickets.seat.sector, tickets.seat.sector.hall,
     * tickets.seat.sector.hall.location, and tickets.performance.hall.location.
     *
     * @param id user id
     * @return list of orders by the user
     */
    @EntityGraph(attributePaths = {
        "tickets", "tickets.performance", "tickets.seat",
        "tickets.performance.event", "tickets.performance.performanceSectors", "tickets.performance.hall",
        "tickets.performance.event.artists",
        "tickets.seat.sector", "tickets.seat.sector.hall",
        "tickets.seat.sector.hall.location", "tickets.performance.hall.location"
    })
    List<Order> getAllOrdersByUserId(Integer id);


    @EntityGraph(attributePaths = {
        "tickets",
        "transactions"
    })
    Order getOrderById(Integer id);
}

