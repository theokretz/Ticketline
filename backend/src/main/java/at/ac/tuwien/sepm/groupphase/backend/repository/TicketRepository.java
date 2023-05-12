package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * The interface Ticket repository.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Find ticket by id.
     *
     * @param id the id of the ticket
     * @return the ticket
     */

    @EntityGraph(attributePaths = {
        "id",
        "seat",
        "seat.row",
        "seat.number",
        "seat.sector",
        "seat.sector.performanceSectors",
        "seat.sector.performanceSectors.price",
        "seat.sector.standing",
        "performance.datetime",
        "performance.event",
        "performance.hall",
        "performance.hall.location.city",
        "performance.hall.location.street"
    })
    Ticket findTicketById(Integer id);
}
