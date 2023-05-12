package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;


public interface PerformanceService {

    /**
     * Find a single performance entry by id.
     *
     * @param id the id of the performance entry
     * @return the performance entry
     */
    Performance findOne(Integer id);
}
