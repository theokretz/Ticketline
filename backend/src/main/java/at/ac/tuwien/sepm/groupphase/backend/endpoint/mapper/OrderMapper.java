package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedOrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleOrderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {

    protected TicketMapper ticketMapper;

    public DetailedOrderDto orderToDetailedOrderDto(Order order) {
        if (order == null) {
            return null;
        }
        Set<Ticket> allTickets = order.getTickets();
        Set<DetailedTicketDto> tickets = new HashSet<>();
        for (Ticket ticket : allTickets) {
            tickets.add(ticketMapper.ticketToDetailedTicket(ticket));
        }
        return DetailedOrderDto.DetailedOrderDtoBuilder.aDetailedOrderDto()
            .withId(order.getId())
            .withCancelled(order.getCancelled())
            .withOrderTs(order.getOrderTs())
            .withTickets(tickets)
            .build();
    }

    public SimpleOrderDto orderToSimpleOrderDto(Order order) {
        if (order == null) {
            return null;
        }
        Set<Ticket> allTickets = order.getTickets();
        Set<OrderTicketDto> tickets = new HashSet<>();
        for (Ticket ticket : allTickets) {
            //price
            BigDecimal price = new BigDecimal(-1);
            for (PerformanceSector performanceSector : ticket.getPerformance().getPerformanceSectors()) {
                if (performanceSector.getSector() == ticket.getSeat().getSector()) {
                    price = performanceSector.getPrice();
                }
            }
            OrderTicketDto orderTicketDto = OrderTicketDto.OrderTicketDtoBuilder.aOrderTicketDto()
                .withId(ticket.getId())
                .withPrice(price)
                .build();
            tickets.add(orderTicketDto);
        }
        return SimpleOrderDto.SimpleOrderDtoBuilder.aSimpleOrderDto()
            .withId(order.getId())
            .withCancelled(order.getCancelled())
            .withOrderTs(order.getOrderTs())
            .withTickets(tickets)
            .build();
    }

    /**
     * Order to order dto.
     *
     * @param order the order
     * @return the order dto
     */
    public abstract OrderDto orderToOrderDto(Order order);
}
