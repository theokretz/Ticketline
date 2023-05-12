package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;

import java.util.List;


public interface CartService {

    /**
     * Put a list of tickets in a users cart.
     *
     * @param tickets the id of the tickets
     * @return list of all created reservations
     */
    List<Reservation> reserveTickets(Integer userId, List<SimpleTicketDto> tickets) throws ConflictException;
}
