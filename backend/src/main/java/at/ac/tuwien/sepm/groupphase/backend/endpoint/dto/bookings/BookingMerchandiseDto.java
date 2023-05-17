package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings;

import jakarta.validation.constraints.NotNull;

public class BookingMerchandiseDto {
    @NotNull
    private Integer id;
    @NotNull
    private Integer quantity;

    public Integer getId() {
        return id;
    }

    private void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    private void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }


    @Override
    public String toString() {
        return "BookingMerchandiseDto{"
            + "merchandiseId=" + id
            + ", quantity=" + quantity
            + '}';
    }
}
