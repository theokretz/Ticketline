import {CartTicket} from './cart-ticket';

export class Cart{
  userId: number;
  tickets: CartTicket[];

  setUserId(id: number): void {
    this.userId = id;
  }
  setCartTickets(tickets: CartTicket[]): void {
    this.tickets = tickets;
  }
}
