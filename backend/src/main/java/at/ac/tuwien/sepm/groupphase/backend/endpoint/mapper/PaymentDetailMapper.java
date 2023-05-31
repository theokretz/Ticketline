package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimplePaymentDetailDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutPaymentDetail;
import at.ac.tuwien.sepm.groupphase.backend.entity.PaymentDetail;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public abstract class PaymentDetailMapper {


    public CheckoutPaymentDetail paymentDetailToCheckoutPaymentDetail(PaymentDetail paymentDetail) {
        CheckoutPaymentDetail checkoutPaymentDetail = new CheckoutPaymentDetail();
        checkoutPaymentDetail.setPaymentDetailId(paymentDetail.getId());
        String cardNumber = paymentDetail.getCardNumber();
        checkoutPaymentDetail.setLastFourDigits(Integer.parseInt(cardNumber.substring(cardNumber.length() - 4)));
        return checkoutPaymentDetail;
    }

    public abstract List<SimplePaymentDetailDto> paymentDetailListToSimplePaymentDetailDtoList(List<PaymentDetail> locations);
}
