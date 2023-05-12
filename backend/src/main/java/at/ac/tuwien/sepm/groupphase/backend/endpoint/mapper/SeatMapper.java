package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.mapstruct.Mapper;


@Mapper
public interface SeatMapper {

    SeatDto seatToDto(Seat seat);

    Seat dtoToSeat(SeatDto seatDto);

}
