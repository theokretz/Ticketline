package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;


/**
 * The interface Order service.
 */
public interface OrderService {
    /**
     * Buy tickets returns order dto.
     *
     * @param userId the user id
     * @return the order dto
     */
    OrderDto buyTickets(Integer userId);
}
