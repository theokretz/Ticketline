package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutDetailsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;

import java.util.List;


/**
 * The interface Cart service.
 */
public interface CartService {
    /**
     * Gets cart.
     *
     * @param userId the user id
     * @return the cart
     */
    List<CartTicketDto> getCart(Integer userId);

    /**
     * Put a list of tickets in a users cart.
     *
     * @param userId  the user id
     * @param tickets the id of the tickets
     * @return list of all created reservations
     * @throws ConflictException the conflict exception
     */
    List<Reservation> reserveTickets(Integer userId, List<SimpleTicketDto> tickets) throws ConflictException;

    /**
     * Delete ticket from cart.
     *
     * @param userId   the user id
     * @param ticketId the ticket id
     */
    void deleteTicketFromCart(Integer userId, Integer ticketId) throws ConflictException;


    List<PaymentDetail> getUserPaymentDetails(Integer userId);

    List<Location> getUserLocations(Integer userId);
}