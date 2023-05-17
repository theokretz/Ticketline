export class PaymentDetail {
  constructor(
    public userId: number,
    public cardHolder: string,
    public cardNumber: number,
    public cvv: number,
    public expirationDate: Date
  ) {}
}

export class CheckoutPaymentDetail {
  paymentDetailId: number;
  lastFourDigits: number;
}
