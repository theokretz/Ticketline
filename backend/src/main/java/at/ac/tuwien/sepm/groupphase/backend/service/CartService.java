package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CartTicketDto;

import java.util.List;

public interface CartService {
    List<CartTicketDto> getCart(Integer userId);
}
