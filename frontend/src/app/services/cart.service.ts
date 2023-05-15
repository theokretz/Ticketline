import {Injectable} from '@angular/core';
import {CartTicket} from '../dtos/cart-ticket';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Cart} from '../dtos/cart';
import {PaymentDetail} from '../dtos/payment-detail';

export interface DialogData {
  paymentDetails: PaymentDetail[];
}
@Injectable({
  providedIn: 'root'
})
export class CartService {
  items: CartTicket[];
  private cartBaseUri: string = this.globals.backendUri;

  constructor(
    private http: HttpClient,
    private globals: Globals
  ) {
  }

  /**
   * Get the tickets in the cart of the specified user
   *
   * @param id the id of the user, whose cart should be fetched
   * @return an observable list of the tickets in the cart of the user
   */
  getCartTickets(id: number): Observable<CartTicket[]> {
    return this.http.get<CartTicket[]>(this.cartBaseUri + '/users/' + id + '/cart');
  }


  /**
   * Remove a ticket from the specified user's cart
   *
   *  @param id the id of the user, from whose cart the ticket should be deleted
   *  @param ticketId the id of the ticket that should be removed from the cart
   *  TODO: still a mock, implement backend
   */
  removeTicketFromCart(id: number, ticketId: number): Observable<CartTicket> {
    // @ts-ignore
    // return this.http.delete(this.cartBaseUri + '/users/' + id + '/cart');
    return new Observable<CartTicket>((subscriber) => {
      subscriber.next();
      subscriber.complete();
    });
  }
  buy(id: number) {
    return this.http.post<void>(this.cartBaseUri + '/bookings', id);
  }
}
