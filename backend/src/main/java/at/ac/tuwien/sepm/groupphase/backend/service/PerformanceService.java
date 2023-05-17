package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceDto;


public interface PerformanceService {

    /**
     * Find a single performance entry by id.
     *
     * @param id the id of the performance entry
     * @return the performance entry
     */
    DetailedPerformanceDto getPerformancePlanById(Integer id);
}
