export class PaymentDetail {
  userId: number;
  cardHolder: string;
  cardNumber: string;
  cvv: number;
  expirationDate: Date;
}

export class CheckoutPaymentDetail {
  paymentDetailId: number;
  lastFourDigits: number;
}
