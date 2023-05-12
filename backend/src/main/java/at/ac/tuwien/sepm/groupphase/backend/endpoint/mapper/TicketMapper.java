package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface TicketMapper {

    @Named("simpleTicket")
    SimpleTicketDto ticketToSimpleTicket(Ticket ticket);


    @IterableMapping(qualifiedByName = "simpleTicket")
    List<SimpleTicketDto> ticketToSimpleTicket(List<Ticket> tickets);
}
