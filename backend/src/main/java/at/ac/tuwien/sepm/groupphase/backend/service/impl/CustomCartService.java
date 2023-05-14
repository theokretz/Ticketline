package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.TicketMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.User;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.NotUserRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ReservationRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomCartService implements CartService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TicketRepository ticketRepository;
    private final NotUserRepository notUserRepository;
    private final ReservationRepository reservationRepository;

    private final TicketMapper ticketMapper;

    public CustomCartService(TicketRepository ticketRepository, NotUserRepository notUserRepository, ReservationRepository reservationRepository,
                             TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.notUserRepository = notUserRepository;
        this.reservationRepository = reservationRepository;
        this.ticketMapper = ticketMapper;
    }


    @Override
    public List<Reservation> reserveTickets(Integer userId, List<SimpleTicketDto> tickets) throws ConflictException {

        Optional<User> user = notUserRepository.findById(userId);

        //TODO: add authentication and check if userId is the same as the logged in user

        if (user.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        List<Ticket> foundTickets = ticketRepository.findTicketsBySimpleTicketDtoList(tickets);
        if (foundTickets.isEmpty()) {
            throw new NotFoundException("No tickets found");
        }
        List<String> conflictMsg = new ArrayList<>();
        if (foundTickets.size() != tickets.size()) {
            List<SimpleTicketDto> foundTicketsDto = ticketMapper.ticketToSimpleTicket(foundTickets);
            tickets.removeAll(foundTicketsDto);
            for (SimpleTicketDto ticket : tickets) {
                conflictMsg.add("Ticket " + ticket.getId() + " not found");
            }
            throw new ConflictException("At least one ticket does not exist", conflictMsg);
        }

        List<Reservation> reserved = new ArrayList<>();
        Map<Integer, List<Ticket>> ticketsBySector = new HashMap<>();
        for (Ticket ticket : foundTickets) {
            // check if ticket is already reserved or bought
            if (ticket.getOrder() != null || !(ticket.getReservation() == null || ticket.getReservation().getExpirationTs().isBefore(LocalDateTime.now()))) {
                // if ticket cannot be reserved, check if it is standing
                Sector sector = ticket.getSeat().getSector();
                if (sector.getStanding()) {
                    // if ticket is standing, find any ticket in the same sector that is not reserved or bought and reserve it
                    if (!ticketsBySector.containsKey(sector.getId())) {
                        // if no ticket in this sector has been fetched, find all tickets in this sector
                        List<Ticket> sectorTickets = ticketRepository.findBySeatSectorId(sector.getId());
                        ticketsBySector.put(sector.getId(), sectorTickets);
                    }
                    // find any ticket in this sector that is not reserved or bought
                    Optional<Ticket> availableTicket = ticketsBySector.get(sector.getId()).stream().filter(t ->
                        t.getOrder() == null
                            && (t.getReservation() == null || t.getReservation().getExpirationTs().isBefore(LocalDateTime.now()))
                    ).findFirst();
                    if (availableTicket.isPresent()) {
                        // if such a ticket exists, reserve it
                        addTicketToReserved(user.get(), reserved, availableTicket.get());
                        ticketsBySector.get(sector.getId()).remove(availableTicket.get());
                        continue;
                    }
                }
                conflictMsg.add("Ticket: " + ticket.getId() + " cannot be reserved, because it is already reserved or bought");
                continue;
            }
            addTicketToReserved(user.get(), reserved, ticket);
        }
        if (conflictMsg.size() > 0) {
            throw new ConflictException("At least one ticket is already reserved or bought", conflictMsg);
        }
        LOGGER.info("reserving tickets {}", tickets);
        reservationRepository.saveAll(reserved);
        return reserved;
    }

    private void addTicketToReserved(User user, List<Reservation> reserved, Ticket ticket) {
        Integer id = ticket.getReservation() == null ? null : ticket.getReservation().getId();
        Reservation reservation = Reservation.ReservationBuilder.aReservation()
            .withId(id)
            .withTicket(ticket)
            .withCart(true)
            .withExpirationTs(LocalDateTime.now().plusMinutes(15))
            .withUser(user)
            .build();
        reserved.add(reservation);
    }

}
