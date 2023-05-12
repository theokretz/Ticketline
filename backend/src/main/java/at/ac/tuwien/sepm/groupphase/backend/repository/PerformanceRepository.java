package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PerformanceRepository extends JpaRepository<Performance, Integer> {

    /**
     * Find a single performance entry by id.
     * with eagerLoading for hall.location, performanceSectors,
     * performanceSectors.sector.seats,performanceSectors.sector.seats,
     * event, tickets, tickets.reservation
     *
     * @param id the id of the performance entry
     * @return the performance entry
     */
    @EntityGraph(attributePaths = {
        "hall.location",
        "performanceSectors",
        "performanceSectors.sector",
        "performanceSectors.sector.seats",
        "event",
        "tickets",
        "tickets.reservation"})
    Optional<Performance> findPerformanceById(Integer id);

}

