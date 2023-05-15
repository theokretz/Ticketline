package at.ac.tuwien.sepm.groupphase.backend.endpoint;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleReservationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ReservationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class CartEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final CartService cartService;
    private final ReservationMapper reservationMapper;

    @Autowired
    public CartEndpoint(CartService cartService, ReservationMapper reservationMapper) {
        this.cartService = cartService;
        this.reservationMapper = reservationMapper;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}/cart")
    @Operation(summary = "get cart of a user", security = @SecurityRequirement(name = "apiKey"))
    public List<CartTicketDto> getCart(@Valid @PathVariable("id") Integer userId) {
        LOGGER.info("GET /api/v1/users/{}/cart", userId);
        try {
            return cartService.getCart(userId);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to get cart: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/{id}/cart/tickets")
    @Operation(summary = "put tickets in the cart", security = @SecurityRequirement(name = "apiKey"))
    public List<SimpleReservationDto> find(@Valid @PathVariable("id") Integer userId, @Valid @RequestBody List<SimpleTicketDto> tickets) {
        //TODO: authenticate that id = userid
        //@RequestHeader("Authorization") String token,
        LOGGER.info("GET /api/v1/users/{}/cart/tickets", tickets);

        try {
            List<Reservation> reservations = cartService.reserveTickets(userId, tickets);
            return reservationMapper.reservationToSimpleReservationDto(reservations);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to buy tickets: " + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ConflictException e) {
            LOGGER.info("Unable to buy tickets: " + e.getMessage());
            HttpStatus status = HttpStatus.CONFLICT;
            throw new ResponseStatusException(status, e.getMessage(), e);

        }
    }
}
