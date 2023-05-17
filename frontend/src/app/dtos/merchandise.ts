export class BookingMerchandise {
  id: number;
  quantity: number;

  constructor(id: number, quantity: number) {
    this.id = id;
    this.quantity = quantity;
  }
}


export class Merchandise {
  id: number;
  price: number;
  pointsPrice?: number;
  pointsReward?: number;
  title: string;
  description?: string;
  quantity?: number;


}
