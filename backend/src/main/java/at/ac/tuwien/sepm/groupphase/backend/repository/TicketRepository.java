package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Collectors;


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

    /**
     * Find all tickets from a list of ticket ids.
     *
     * @param ids the ticket ids
     * @return the ticket list
     */
    @EntityGraph(attributePaths = {"reservation"})
    @Query("SELECT t FROM Ticket t WHERE t.id IN :ids")
    List<Ticket> findAllByIds(@Param("ids") List<Integer> ids);

    /**
     * Find all tickets from a list of ticket ids.
     *
     * @param simpleTickets the event id
     * @return the ticket list
     */
    default List<Ticket> findTicketsBySimpleTicketDtoList(List<SimpleTicketDto> simpleTickets) {
        List<Integer> ids = simpleTickets.stream().map(SimpleTicketDto::getId).collect(Collectors.toList());
        return this.findAllByIds(ids);
    }
}
