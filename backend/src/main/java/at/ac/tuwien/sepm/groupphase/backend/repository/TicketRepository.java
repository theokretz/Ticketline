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
        "seat",
        "seat.sector",
        "performance.event",
        "performance.hall",
        "performance.hall.location",
        "performance.performanceSectors",
        "performance.performanceSectors.sector"
    })
    Ticket findTicketById(Integer id);

    /**
     * Find all tickets from a list of ticket ids.
     *
     * @param ids the ticket ids
     * @return the ticket list
     */
    @EntityGraph(attributePaths = {"reservation", "seat", "seat.sector"})
    @Query("SELECT t FROM Ticket t WHERE t.id IN :ids")
    List<Ticket> findAllByIds(@Param("ids") List<Integer> ids);

    /**
     * Find all tickets from a list of ticket ids with performance and performanceSectors.
     *
     * @param ids the ticket ids
     * @return the ticket list
     */
    @EntityGraph(attributePaths = {"reservation", "seat", "seat.sector", "performance", "performance.performanceSectors"})
    @Query("SELECT t FROM Ticket t JOIN FETCH t.performance JOIN FETCH t.performance.performanceSectors WHERE t.id IN :ids")
    List<Ticket> findAllByIdsWithPerformance(@Param("ids") List<Integer> ids);


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

    /**
     * Find all tickets without an order associated.
     *
     * @return the ticket list
     */
    List<Ticket> findAllByOrderIsNull();

    /**
     * Find all tickets without an order or reservation associated.
     *
     * @return the ticket list
     */
    List<Ticket> findAllByOrderIsNullAndReservationIsNull();

    /**
     * Find all tickets that are in the sector with the given sector id.
     *
     * @param id the sector id
     * @return the ticket list
     */
    @EntityGraph(attributePaths = {"performance", "seat", "seat.sector"})
    List<Ticket> findBySeatSectorId(Integer id);

    /**
     * Find all tickets matching the id.
     *
     * @param id the ticket id
     * @return the ticket list
     */
    @EntityGraph(attributePaths = {"performance", "seat", "seat.sector"})
    List<Ticket> findByOrderId(Integer id);
}
