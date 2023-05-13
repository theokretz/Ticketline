package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;

public class CartDto {
    private Integer userId;
    private List<CartTicketDto> tickets;

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public List<CartTicketDto> getTickets() {
        return tickets;
    }

    public void setTickets(List<CartTicketDto> tickets) {
        this.tickets = tickets;
    }

    @Override
    public String toString() {
        return "CartDto{"
            +
            "id=" + userId
            +
            ", seats=" + tickets
            +
            '}';
    }
}
