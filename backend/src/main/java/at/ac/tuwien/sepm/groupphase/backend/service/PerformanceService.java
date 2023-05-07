package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UserDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Order;


public interface PerformanceService {
    //TODO JavaDoc
    Order buyTickets(CartDto cartDto, int performanceId, UserDto userDto);
}
