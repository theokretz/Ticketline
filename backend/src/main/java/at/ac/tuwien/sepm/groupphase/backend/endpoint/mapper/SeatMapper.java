package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;

@Mapper
public abstract class SeatMapper {

    @Autowired
    protected TicketRepository ticketRepository;

    public PerformanceTicketDto[][] performanceSectorToReservedSeat(Set<PerformanceSector> performanceSector) {
        int maxRow = performanceSector.stream().mapToInt(ps -> ps.getSector().getSeats().stream().mapToInt(Seat::getRow).max().orElse(0)).max().orElse(0);
        int maxNumber = performanceSector.stream().mapToInt(ps -> ps.getSector().getSeats().stream().mapToInt(Seat::getNumber).max().orElse(0)).max().orElse(0);
        PerformanceTicketDto[][] performanceTicketDtos = new PerformanceTicketDto[maxRow + 1][maxNumber + 1];

        for (PerformanceSector ps : performanceSector) {
            for (Seat s : ps.getSector().getSeats()) {
                performanceTicketDtos[s.getRow()][s.getNumber()] = PerformanceTicketDto.PerformanceTicketBuilder.aPerformanceTicket()
                    .withRow(s.getRow())
                    .withNumber(s.getNumber())
                    .withSectorId(ps.getSector().getId())
                    .build();
            }
        }
        return performanceTicketDtos;
    }

    public PerformanceTicketDto[][] performanceToReservedSeat(Performance performance) {
        Set<PerformanceSector> performanceSector = performance.getPerformanceSectors();
        int maxRow = performanceSector.stream().mapToInt(ps -> ps.getSector().getSeats().stream().mapToInt(Seat::getRow).max().orElse(0)).max().orElse(0);
        int maxNumber = performanceSector.stream().mapToInt(ps -> ps.getSector().getSeats().stream().mapToInt(Seat::getNumber).max().orElse(0)).max().orElse(0);
        PerformanceTicketDto[][] performanceTicketDtos = new PerformanceTicketDto[maxRow + 1][maxNumber + 1];

        Set<Ticket> tickets = performance.getTickets();

        for (Ticket ticket : tickets) {
            Seat seat = ticket.getSeat();
            boolean reserved = true;
            if (ticket.getOrder() == null) {
                if (ticket.getReservation() == null) {
                    reserved = false;
                } else if (ticket.getReservation().getExpirationTs().isBefore(LocalDateTime.now())) {
                    reserved = false;
                }
            }
            performanceTicketDtos[seat.getRow()][seat.getNumber()] = PerformanceTicketDto.PerformanceTicketBuilder.aPerformanceTicket()
                .withRow(seat.getRow())
                .withNumber(seat.getNumber())
                .withSectorId(seat.getSector().getId())
                .withReserved(reserved)
                .withTicketId(ticket.getId())
                .build();
        }
        return performanceTicketDtos;
    }
}
