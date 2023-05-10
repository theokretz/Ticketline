package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * The interface Performance repository.
 */
public interface PerformanceRepository extends JpaRepository<Performance, Integer> {

    /**
     * Find performance by id.
     *
     * @param id the id of the performance
     * @return the performance
     */
    Performance findPerformanceById(Integer id);
}

