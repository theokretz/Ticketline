package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;


public interface PerformanceService {
    //TODO JavaDoc
    OrderDto buyTickets(CartDto cartDto, int performanceId, UserDto userDto);
}
