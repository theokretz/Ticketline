package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class SeatMapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public CartSeatDto seatToCartSeatDto(Seat seat) {
        LOGGER.trace("seatToCartSeatDto({})", seat);
        CartSeatDto cartSeatDto = new CartSeatDto();
        cartSeatDto.setId(seat.getId());
        cartSeatDto.setNumber(seat.getNumber());
        cartSeatDto.setRow(seat.getRow());
        cartSeatDto.setSector(seat.getSector());
        if (seat.getTickets() != null) {
            cartSeatDto.setTickets(seat.getTickets());
        }
        return cartSeatDto;
    }

    public Seat cartSeatDtoToSeat(CartSeatDto cartSeatDto) {
        LOGGER.trace("cartSeatDtoToSeat({})", cartSeatDto);
        Seat seat = new Seat();
        seat.setId(cartSeatDto.getId());
        seat.setNumber(cartSeatDto.getNumber());
        seat.setRow(cartSeatDto.getRow());
        seat.setSector(cartSeatDto.getSector());
        if (cartSeatDto.getTickets() != null) {
            seat.setTickets(cartSeatDto.getTickets());
        }
        return seat;
    }
}
