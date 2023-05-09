package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Component
public class OrderMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public OrderDto orderToDto(Order order) {
        LOGGER.trace("orderToDto({})", order);
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
