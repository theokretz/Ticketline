package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


/**
 * The interface Performance repository.
 */
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {

    Optional<Performance> findAllPerformancesById(Integer id);

    @EntityGraph(attributePaths = {
        "event", "hall", "event.artists", "hall.location"
    })
    List<Performance> findAllByEvent_Id(Integer id);

    @EntityGraph(attributePaths = {
        "event", "hall", "hall.location"
    })
    @Query("SELECT a FROM Performance a WHERE a.hall.location.id = :id")
    List<Performance> findAllByHallLocation_Id(@Param("id") Integer id);
}

