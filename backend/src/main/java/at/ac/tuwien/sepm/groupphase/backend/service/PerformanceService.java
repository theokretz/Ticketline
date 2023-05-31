package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;

import java.util.List;


public interface PerformanceService {

    /**
     * Find a single performance entry by id.
     *
     * @param id the id of the performance entry
     * @return the performance entry
     */
    DetailedPerformanceDto getPerformancePlanById(Integer id);

    /**

     * Get the performances of the event specified by the given id.
     *
     * @param id the id of the event whose performances should be fetched
     * @return a list of performances of the event matching the given id
     */
    List<Performance> getPerformancesOfEventById(Integer id);
}
