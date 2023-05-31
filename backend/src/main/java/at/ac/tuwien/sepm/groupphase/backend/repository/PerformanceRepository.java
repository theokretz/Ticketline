package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

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
}

