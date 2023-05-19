package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleOrderDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class OrderHistoryEndpoint {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final OrderService orderService;

    @Autowired
    public OrderHistoryEndpoint(OrderService orderService) {
        this.orderService = orderService;
    }

    @PermitAll // TEMPORARY, prevents security issues by allowing all requests
    @GetMapping("{id}/order-history")
    public List<SimpleOrderDto> getOrderHistory(@PathVariable Integer id) {
        LOG.info("GET /api/v1/users/{}/order-history", id);
        try {
            List<SimpleOrderDto> output =  orderService.getOrderHistory(id);
            LOG.info("{}", output);
            return output;
        } catch (NotFoundException e) {
            HttpStatus status = HttpStatus.NOT_FOUND;
            logClientError(status, "User to get history from not found", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        } catch (ValidationException e) {
            HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
            logClientError(status, "Order history validation failed", e);
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }

    private void logClientError(HttpStatus status, String message, Exception e) {
        LOG.warn("{} {}: {}: {}", status.value(), message, e.getClass().getSimpleName(), e.getMessage());
    }
}
