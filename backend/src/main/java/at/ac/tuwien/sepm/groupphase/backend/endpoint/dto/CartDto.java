package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;

public class CartDto {
    int id;
    List<CartTicketDto> tickets;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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
            "id=" + id
            +
            ", seats=" + tickets
            +
            '}';
    }
}
