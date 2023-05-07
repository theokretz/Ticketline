package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import java.util.List;

public class CartDto {

    int standing;

    List<CartSeatDto> seats;

    public int getStanding() {
        return standing;
    }

    public void setStanding(int standing) {
        this.standing = standing;
    }

    public List<CartSeatDto> getSeats() {
        return seats;
    }

    public void setSeats(List<CartSeatDto> seats) {
        this.seats = seats;
    }
}
