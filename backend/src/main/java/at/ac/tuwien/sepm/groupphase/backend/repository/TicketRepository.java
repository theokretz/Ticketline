package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
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
    Ticket findTicketById(Integer id);
}
