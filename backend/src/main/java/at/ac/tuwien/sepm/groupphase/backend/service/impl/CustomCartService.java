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
import java.time.LocalDateTime;
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
    }

    @Override
    public List<CartTicketDto> getCart(Integer userId) {
        LOGGER.debug("Get Cart of User {}", userId);
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
                int seatRow = ticket.getSeat().getRow();
                int seatNumber = ticket.getSeat().getNumber();
                String sectorName = ticket.getSeat().getSector().getName();
                boolean standing = ticket.getSeat().getSector().getStanding();
                LocalDateTime date = ticket.getPerformance().getDatetime();
                String eventName = ticket.getPerformance().getEvent().getName();
                String hallName = ticket.getPerformance().getHall().getName();
                String locationCity = ticket.getPerformance().getHall().getLocation().getCity();
                String locationStreet = ticket.getPerformance().getHall().getLocation().getStreet();

                //price
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
                    .withSeatRow(seatRow)
                    .withSeatNumber(seatNumber)
                    .withSectorName(sectorName)
                    .withStanding(standing)
                    .withDate(date)
                    .withEventName(eventName)
                    .withHallName(hallName)
                    .withLocationCity(locationCity)
                    .withLocationStreet(locationStreet)
                    .withPrice(price)
                    .build();
                tickets.add(cartTicketDto);
            }
        }
        return tickets;
    }
}
