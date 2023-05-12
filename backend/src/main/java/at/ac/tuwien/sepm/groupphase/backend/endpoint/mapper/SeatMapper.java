package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.mapstruct.Mapper;


/**
 * The interface Seat mapper.
 */
@Mapper
public interface SeatMapper {

    /**
     * Seat to seat dto.
     *
     * @param seat the seat
     * @return the seat dto
     */
    SeatDto seatToDto(Seat seat);

    /**
     * Dto to seat.
     *
     * @param seatDto the seat dto
     * @return the seat
     */
    Seat dtoToSeat(SeatDto seatDto);

}
