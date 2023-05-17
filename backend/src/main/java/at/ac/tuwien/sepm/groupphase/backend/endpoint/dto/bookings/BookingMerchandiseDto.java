package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings;

import jakarta.validation.constraints.NotNull;

public class BookingMerchandiseDto {
    @NotNull
    private Integer merchandiseId;
    @NotNull
    private Integer quantity;

    public Integer getMerchandiseId() {
        return merchandiseId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    private void setMerchandiseId(Integer merchandiseId) {
        this.merchandiseId = merchandiseId;
    }

    private void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
