package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.time.LocalDateTime;
import java.util.Set;

public class SimpleOrderDto {
    private Integer id;
    private LocalDateTime orderTs;
    private Boolean cancelled;
    private Set<OrderTicketDto> tickets;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getOrderTs() {
        return orderTs;
    }

    public void setOrderTs(LocalDateTime orderTs) {
        this.orderTs = orderTs;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Set<OrderTicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(Set<OrderTicketDto> tickets) {
        this.tickets = tickets;
    }

    public static final class SimpleOrderDtoBuilder {
        private Integer id;
        private LocalDateTime orderTs;
        private Boolean cancelled;
        private Set<OrderTicketDto> tickets;

        private SimpleOrderDtoBuilder() {
        }

        public static SimpleOrderDtoBuilder aSimpleOrderDto() {
            return new SimpleOrderDtoBuilder();
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withOrderTs(LocalDateTime orderTs) {
            this.orderTs = orderTs;
            return this;
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withCancelled(Boolean cancelled) {
            this.cancelled = cancelled;
            return this;
        }

        public SimpleOrderDto.SimpleOrderDtoBuilder withTickets(Set<OrderTicketDto> tickets) {
            this.tickets = tickets;
            return this;
        }

        public SimpleOrderDto build() {
            SimpleOrderDto detailedOrderDto = new SimpleOrderDto();
            detailedOrderDto.setId(id);
            detailedOrderDto.setOrderTs(orderTs);
            detailedOrderDto.setCancelled(cancelled);
            detailedOrderDto.setTickets(tickets);
            return detailedOrderDto;
        }
    }
}
