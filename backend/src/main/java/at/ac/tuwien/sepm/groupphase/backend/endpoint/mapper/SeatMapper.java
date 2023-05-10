package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class SeatMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SeatDto seatToDto(Seat seat) {
        LOGGER.trace("SeatDto({})", seat);
        SeatDto seatDto = new SeatDto();
        seatDto.setId(seat.getId());
        seatDto.setNumber(seat.getNumber());
        seatDto.setRow(seat.getRow());
        seatDto.setSector(seat.getSector());
        if (seat.getTickets() != null) {
            seatDto.setTickets(seat.getTickets());
        }
        return seatDto;
    }

    public Seat dtoToSeat(SeatDto seatDto) {
        LOGGER.trace("dtoToSeat({})", seatDto);
        Seat seat = new Seat();
        seat.setId(seatDto.getId());
        seat.setNumber(seatDto.getNumber());
        seat.setRow(seatDto.getRow());
        seat.setSector(seatDto.getSector());
        if (seatDto.getTickets() != null) {
            seat.setTickets(seatDto.getTickets());
        }
        return seat;
    }
}
