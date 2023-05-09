package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.service.PerformanceService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;


@RestController
public class PerformanceEndpoint {
    private final PerformanceService performanceService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    public PerformanceEndpoint(PerformanceService performanceService) {
        this.performanceService = performanceService;
    }

    @Secured("ROLE_USER")
    @PostMapping
    @Operation(summary = "Buy Tickets from Cart")
    public OrderDto buySeats(@RequestBody CartDto cartDto, int performanceId, UserDto userDto) {
        LOGGER.info("POST /api/v1/bookings  cart: {}, performanceId{}, user: {}", cartDto, performanceId, userDto);
        return this.performanceService.buyTickets(cartDto, performanceId, userDto);
    }
}
