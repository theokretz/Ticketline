package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class CustomCartService implements CartService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TicketRepository ticketRepository;
    private final NotUserRepository notUserRepository;
    private final ReservationRepository reservationRepository;


    public CustomCartService(TicketRepository ticketRepository, NotUserRepository notUserRepository, ReservationRepository reservationRepository
    ) {
        this.ticketRepository = ticketRepository;
        this.notUserRepository = notUserRepository;
        this.reservationRepository = reservationRepository;
        ;
    }

    @Override
    public List<CartTicketDto> getCart(Integer userId) {
        Optional<ApplicationUser> optionalApplicationUser = notUserRepository.findById(userId);
        if (optionalApplicationUser.isEmpty()) {
            throw new NotFoundException("Could not find User");
        }
        ApplicationUser user = optionalApplicationUser.get();
        List<CartTicketDto> tickets = new ArrayList<>();
        Set<Reservation> reservationSet = user.getReservations();

        for (Reservation r : reservationSet) {
            if (r.getCart()) {
                Ticket ticket = r.getTicket();
                BigDecimal price = new BigDecimal(-1);
                for (PerformanceSector perfSector : ticket.getSeat().getSector().getPerformanceSectors()) {
                    if (perfSector.getPerformance() == ticket.getPerformance()) {
                        price = perfSector.getPrice();
                    }
                }
                if (Objects.equals(price, BigDecimal.valueOf(-1))) {
                    throw new FatalException("No Performance Sector assigned");
                }

                CartTicketDto cartTicketDto = CartTicketDto.CartTicketDtoBuilder.aCartTicketDto()
                    .withId(ticket.getId())
                    .withSeatRow(ticket.getSeat().getRow())
                    .withSeatNumber(ticket.getSeat().getNumber())
                    .withSectorName(ticket.getSeat().getSector().getName())
                    .withStanding(ticket.getSeat().getSector().getStanding())
                    .withDate(ticket.getPerformance().getDatetime())
                    .withEventName(ticket.getPerformance().getEvent().getName())
                    .withHallName(ticket.getPerformance().getHall().getName())
                    .withLocationCity(ticket.getPerformance().getHall().getLocation().getCity())
                    .withLocationStreet(ticket.getPerformance().getHall().getLocation().getStreet())
                    .withPrice(price)
                    .build();
                tickets.add(cartTicketDto);
            }
        }
        return tickets;
    }
}
