export class PaymentDetail {
  constructor(
    public userId: number,
    public cardHolder: string,
    public cardNumber: number,
    public cvv: number,
    public expirationDate: Date
  ) {
  }
}
