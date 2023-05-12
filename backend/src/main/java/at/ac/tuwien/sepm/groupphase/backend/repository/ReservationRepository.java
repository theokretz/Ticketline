package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
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
}
