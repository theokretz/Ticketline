package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderDto orderToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setOrderTs(order.getOrderTs());
        dto.setCancelled(order.getCancelled());
        dto.setMerchandiseOrdered(order.getMerchandiseOrdered());
        dto.setDeliveryAdress(order.getDeliveryAdress());
        dto.setPaymentDetail(order.getPaymentDetail());
        dto.setUser(order.getUser());
        dto.setTickets(order.getTickets());
        dto.setTransactions(order.getTransactions());
        return dto;
    }
}
