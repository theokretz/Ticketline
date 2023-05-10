package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;


/**
 * The interface Order service.
 */
public interface OrderService {
    /**
     * Buy tickets returns order dto.
     *
     * @param cartDto the cart dto
     * @param userDto the user dto
     * @return the order dto
     */
    OrderDto buyTickets(CartDto cartDto, UserDto userDto);
}
