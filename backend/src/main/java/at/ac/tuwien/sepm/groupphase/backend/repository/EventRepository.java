package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface EventRepository extends JpaRepository<Event, Integer> {

    @EntityGraph(attributePaths = {
        "artists"
    })
    List<Event> findEventsByArtistsId(@Param("artistId")Integer id);
}

