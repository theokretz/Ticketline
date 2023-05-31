package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderHistoryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Transaction;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;

import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Comparator;

@Mapper(componentModel = "spring")
public abstract class OrderMapper {
    public OrderHistoryDto orderToOrderHistoryDto(Order order) {
        if (order == null) {
            return null;
        }
        List<Ticket> allTickets = new ArrayList<>(order.getTickets());
        List<OrderTicketDto> tickets = new ArrayList<>();
        List<OrderMerchandiseDto> merchandises = new ArrayList<>();
        Set<Transaction> allTransactions = order.getTransactions();
        // all tickets
        BigDecimal totalPrice = new BigDecimal(0);
        Integer totalPoints = 0;
        for (Ticket ticket : allTickets) {
            // price
            BigDecimal price = new BigDecimal(-1);
            for (PerformanceSector performanceSector : ticket.getPerformance().getPerformanceSectors()) {
                if (performanceSector.getSector() == ticket.getSeat().getSector()) {
                    price = performanceSector.getPrice();
                }
            }

            // totalPrice = totalPrice.add(price);

            // artists
            // format the set of artists to a string, but only show at most 3 artists!
            String artistsFormatted = "";
            int i = 0;
            // Set<Artist> artists = ticket.getPerformance().getEvent().getArtists();
            List<Artist> artists = new ArrayList<>(ticket.getPerformance().getEvent().getArtists());
            artists.sort(Comparator.comparing(Artist::getName));
            int artistsSize = artists.size();
            for (Artist artist : artists) {
                artistsFormatted += artist.getName();
                if (i < 3) {
                    if (artistsSize >= 3) {
                        artistsFormatted += ", ";
                    } else if (i < artistsSize - 1) {
                        artistsFormatted += ", ";
                    } else {
                        break;
                    }
                }
                if (i == 2) {
                    if (artistsSize != 3) {
                        artistsFormatted += "...";
                    }
                    break;
                }
                i++;
            }

            OrderTicketDto orderTicketDto = OrderTicketDto.OrderTicketDtoBuilder.aOrderTicketDto()
                .withId(ticket.getId())
                .withPrice(price)
                .withArtists(artistsFormatted)
                .withEventName(ticket.getPerformance().getEvent().getName())
                .withDatetime(ticket.getPerformance().getDatetime())
                .build();
            tickets.add(orderTicketDto);
        }

        // total price and points
        totalPrice = new BigDecimal(0);
        if (allTransactions != null) {
            for (Transaction transaction : allTransactions) {
                totalPrice = totalPrice.add(transaction.getDeductedAmount());
                totalPoints += transaction.getDeductedPoints();
            }
        }

        // all merchandises
        List<MerchandiseOrdered> allMerchandise = new ArrayList<>(order.getMerchandiseOrdered());
        for (MerchandiseOrdered merchandiseOrdered : allMerchandise) {
            OrderMerchandiseDto orderMerchandiseDto = OrderMerchandiseDto.OrderMerchandiseDtoBuilder.aOrderMerchandiseDto()
                .withId(merchandiseOrdered.getId())
                .withItemName(merchandiseOrdered.getMerchandise().getTitle())
                .withQuantity(merchandiseOrdered.getQuantity())
                .withPrice(merchandiseOrdered.getMerchandise().getPrice())
                .build();
            merchandises.add(orderMerchandiseDto);
            // totalPrice = totalPrice.add(merchandiseOrdered.getMerchandise().getPrice().multiply(new BigDecimal(merchandiseOrdered.getQuantity())));
        }
        // sort tickets list by id
        tickets.sort(Comparator.comparing(OrderTicketDto::getId));
        // sort merchandises list by id
        merchandises.sort(Comparator.comparing(OrderMerchandiseDto::getId));

        return OrderHistoryDto.OrderHistoryDtoBuilder.aOrderHistoryDto()
            .withId(order.getId())
            .withOrderTs(order.getOrderTs())
            .withCancelled(order.getCancelled())
            .withTickets(tickets)
            .withMerchandises(merchandises)
            .withTotalPrice(totalPrice)
            .withTotalPoints(totalPoints)
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
