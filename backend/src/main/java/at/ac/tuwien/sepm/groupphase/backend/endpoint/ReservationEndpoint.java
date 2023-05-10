package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class ReservationEndpoint {
    private ReservationRepository reservationRepository;

    @Autowired
    public ReservationEndpoint(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Collection<TicketDto> getCart(UserDto userDto) {
        return null;
    }

    //Post
    public void addTicketToCart(TicketDto ticketDto, UserDto userdto) {
        //reservation erstellen
    }
}
