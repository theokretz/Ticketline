package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutLocation;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutPaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.LocationMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.PaymentDetailMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users")
public class UserEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;
    private final LocationMapper locationMapper;
    private final PaymentDetailMapper paymentDetailMapper;

    @Autowired
    public UserEndpoint(UserService userService, LocationMapper locationMapper, PaymentDetailMapper paymentDetailMapper) {
        this.userService = userService;
        this.locationMapper = locationMapper;
        this.paymentDetailMapper = paymentDetailMapper;
    }


    /*@PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}/locations")
    @Operation(summary = "get Location from user", security = @SecurityRequirement(name = "apiKey"))
    public List<LocationDto> getUserLocation(@Valid @PathVariable Integer id) {
        // TODO authentication
        List<Location> locations = this.userService.getUserLocations(id);
        return this.locationMapper.locationListToLocationDtoList(locations);
    }*/

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/locations")
    @Operation(summary = "update Location from user", security = @SecurityRequirement(name = "apiKey"))
    public CheckoutLocation updateUserLocation(@Valid @PathVariable Integer id, @RequestBody LocationDto locationDto, Authentication auth) {
        if (!userService.isUserAuthenticated(id, auth)) {
            LOGGER.info("Unauthorized update");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            Location saved =  this.userService.updateUserLocation(id, locationDto);
            return this.locationMapper.locationToCheckoutLocation(saved);
        } catch (ValidationException e) {
            LOGGER.info("Request was formatted incorrectly:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    /*@PermitAll
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{userId}/locations/{locationId}")
    @Operation(summary = "delete Location from user", security = @SecurityRequirement(name = "apiKey"))
    public void deleteUserLocation(@Valid @PathVariable Integer userId, @PathVariable Integer locationId) {
        // TODO authentication
        this.userService.deleteUserLocation(userId, locationId);
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}/payment-details")
    @Operation(summary = "get Payment Detail from user", security = @SecurityRequirement(name = "apiKey"))
    public List<SimplePaymentDetailDto> getUserPaymentDetails(@Valid @PathVariable Integer id) {
        // TODO authentication
        List<PaymentDetail> paymentDetails = this.userService.getUserPaymentDetails(id);
        return this.paymentDetailMapper.paymentDetailListToSimplePaymentDetailDtoList(paymentDetails);
    }*/

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{id}/payment-details")
    @Operation(summary = "update Payment Detail from user", security = @SecurityRequirement(name = "apiKey"))
    public CheckoutPaymentDetail updateUserPaymentDetails(@Valid @PathVariable Integer id, @RequestBody SimplePaymentDetailDto paymentDetails, Authentication auth) {
        if (!userService.isUserAuthenticated(id, auth)) {
            LOGGER.info("Unauthorized update");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized update");
        }
        try {
            PaymentDetail saved = this.userService.updateUserPaymentDetails(id, paymentDetails);
            return this.paymentDetailMapper.paymentDetailToCheckoutPaymentDetail(saved);
        } catch (NotFoundException e) {
            LOGGER.info("Cannot create new payment detail: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidationException e) {
            LOGGER.info("Cannot create new payment detail:" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage(), e);
        }
    }

    /*@PermitAll
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/users/{userId}/payment-details/{paymentDetailsId}")
    @Operation(summary = "delete payment Detail from user", security = @SecurityRequirement(name = "apiKey"))
    public void deleteUserPaymentDetails(@Valid @PathVariable Integer userId, @PathVariable Integer paymentDetailsId) {
        LOGGER.info("/api/v1/users/{}/payment-details/{}", userId, paymentDetailsId);
        // TODO authentication
        this.userService.deleteUserPaymentDetails(userId, paymentDetailsId);
    }*/
}
