package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.topten.EventTicketCountDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Integer> {

    @EntityGraph(attributePaths = {
        "artists"
    })
    List<Event> findEventsByArtistsId(@Param("artistId") Integer id);


    /**
     * find top ten events by tickets sold in the last 30 days per category.
     * sorted by category and tickets sold.
     *
     * @return list of top10 events by category
     */
    @Query(value =
        "select new at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.topten.EventTicketCountDto(id, name, type, "
            + "ticketCount, imagePath) "
            + " from (select e.id as id, e.name as name, e.type as type, count(t.id) as ticketCount, e.imagePath as imagePath, "
            + "ROW_NUMBER() OVER (PARTITION BY e.type order by count(t.id) desc) as row_num "
            + "from Event e "
            + "left join Performance p on e.id = p.event.id "
            + "left join Ticket t on p.id = t.performance.id "
            + "left join Order o on t.order.id = o.id "
            + "where t.order is not null "
            + "and o.orderTs >= DATEADD(month, -1, current_date ) "
            + "group by e.id, e.type "
            + "order by e.type, count(t.id) desc) "
            + "where row_num <= 10")
    List<EventTicketCountDto> findTopTenEventsByTicketCount();
}

