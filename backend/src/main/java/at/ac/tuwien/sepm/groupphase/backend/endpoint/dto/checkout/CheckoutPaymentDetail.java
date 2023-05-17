package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout;

public class CheckoutPaymentDetail {
    private Integer paymentDetailId;
    private Integer lastFourDigits;

    public Integer getPaymentDetailId() {
        return paymentDetailId;
    }

    public void setPaymentDetailId(Integer paymentDetailId) {
        this.paymentDetailId = paymentDetailId;
    }

    public Integer getLastFourDigits() {
        return lastFourDigits;
    }

    public void setLastFourDigits(Integer lastFourDigits) {
        this.lastFourDigits = lastFourDigits;
    }
}
