package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Reservation repository.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    /**
     * Find reservation by user id.
     *
     * @param id the id of the user
     * @return the list of reservations/the cart of the user
     */


    List<Reservation> findReservationByUserId(Integer id);


    /**
     * Find all reservations by user id and cart status.
     * EntityGraph with ticket, ticket.seat, ticket.performance, ticket.performance.event
     *
     * @param id   the id of the user
     * @param cart if the reservation is in the cart or not
     * @return the list of reservations/the cart of the user
     */
    @EntityGraph(attributePaths = {"ticket", "ticket.seat", "ticket.performance", "ticket.performance.event"})
    List<Reservation> findAllWithNamedReservationByUserIdAndCart(Integer id, Boolean cart);

}
