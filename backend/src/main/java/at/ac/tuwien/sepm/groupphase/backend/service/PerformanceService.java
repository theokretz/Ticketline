package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;


/**
 * The interface Performance service.
 */
public interface PerformanceService {
    /**
     * Buy tickets returns order dto.
     *
     * @param cartDto       the cart dto
     * @param performanceId the performance id
     * @param userDto       the user dto
     * @return the order dto
     */
    OrderDto buyTickets(CartDto cartDto, int performanceId, UserDto userDto);
}
