package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;

public class CartDto {
    int id;
    List<CartSeatDto> seats;
    PerformanceDto performanceDto;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<CartSeatDto> getSeats() {
        return seats;
    }

    public void setSeats(List<CartSeatDto> seats) {
        this.seats = seats;
    }

    @Override
    public String toString() {
        return "CartDto{"
            +
            "id=" + id
            +
            ", seats=" + seats
            +
            ", performanceDto=" + performanceDto
            +
            '}';
    }
}
