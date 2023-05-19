package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedReservationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ReservationMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.lang.invoke.MethodHandles;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users/{id}/reservations")
public class ReservationEndpoint {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    public ReservationEndpoint(ReservationService reservationService, ReservationMapper reservationMapper) {
        this.reservationService = reservationService;

        this.reservationMapper = reservationMapper;
    }


    @PermitAll
    @GetMapping(value = "")
    @Operation(summary = "get user reservations data", security = @SecurityRequirement(name = "apiKey"))
    public List<DetailedReservationDto> findUserReservations(@Valid @PathVariable("id") Integer userId) {
        //TODO: authenticate that id = userid

        try {
            List<Reservation> reservations = reservationService.findReservationsByUserIdAndCart(userId, false);
            List<DetailedReservationDto> reservationDto = reservationMapper.reservationToDetailedReservationDto(reservations);
            return reservationDto;
        } catch (NotFoundException e) {
            LOGGER.warn("Unable to find reservations" + e.getMessage());
            HttpStatus status = HttpStatus.NOT_FOUND;
            throw new ResponseStatusException(status, e.getMessage(), e);
        }
    }
}
