package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.math.BigDecimal;

public class OrderTicketDto {
    private Integer id;
    private BigDecimal price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public static final class OrderTicketDtoBuilder {
        private Integer id;
        private BigDecimal price;

        private OrderTicketDtoBuilder() {
        }

        public static OrderTicketDto.OrderTicketDtoBuilder aOrderTicketDto() {
            return new OrderTicketDto.OrderTicketDtoBuilder();
        }

        public OrderTicketDto.OrderTicketDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public OrderTicketDto.OrderTicketDtoBuilder withPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public OrderTicketDto build() {
            OrderTicketDto orderTicketDto = new OrderTicketDto();
            orderTicketDto.setId(id);
            orderTicketDto.setPrice(price);
            return orderTicketDto;
        }
    }

}
