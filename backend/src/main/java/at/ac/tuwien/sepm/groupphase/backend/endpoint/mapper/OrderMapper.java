package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;
import org.mapstruct.Mapper;

/**
 * The interface Order mapper.
 */
@Mapper
public interface OrderMapper {
    /**
     * Order to order dto.
     *
     * @param order the order
     * @return the order dto
     */
    OrderDto orderToOrderDto(Order order);
}
