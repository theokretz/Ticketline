package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleOrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;

import java.util.List;

/**
 * The interface Order service.
 */
public interface OrderService {

    List<SimpleOrderDto> getOrderHistory(Integer id);

    /**
     * Buy tickets returns order dto.
     *
     * @param userId the user id
     * @return the order dto
     */
    OrderDto buyTickets(Integer userId, BookingDto bookingDto) throws ConflictException, ValidationException;
}
