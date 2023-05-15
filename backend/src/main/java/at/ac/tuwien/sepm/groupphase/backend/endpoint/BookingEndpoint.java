package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(value = "/api/v1/bookings")
public class BookingEndpoint {
    private final OrderService orderService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CartEndpoint cartEndpoint;

    @Autowired
    public BookingEndpoint(OrderService orderService, CartEndpoint cartEndpoint) {
        this.orderService = orderService;
        this.cartEndpoint = cartEndpoint;
    }

    @PermitAll
    @PostMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Buy Tickets from Cart", security = @SecurityRequirement(name = "apiKey"))
    public OrderDto buyTickets(@RequestBody Integer userId) {
        LOGGER.info("POST /api/v1/bookings  cart: {}", userId);
        try {
            return this.orderService.buyTickets(userId);
        } catch (NotFoundException e) {
            LOGGER.info("Unable to buy Tickets: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
