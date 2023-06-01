import { Injectable } from '@angular/core';
import { CartTicket } from '../dtos/ticket';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../global/globals';
import { CheckoutPaymentDetail } from '../dtos/payment-detail';
import { Booking } from '../dtos/booking';
import { CheckoutDetails } from '../dtos/checkout-details';
import { CheckoutLocation } from '../dtos/location';
import { BookingMerchandise, Merchandise } from '../dtos/merchandise';
import { Cart } from '../dtos/cart';
import { OrderResponse } from '../dtos/order';

export interface DialogData {
  paymentDetails: CheckoutPaymentDetail[];
  locations: CheckoutLocation[];
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  items: CartTicket[];
  cart: Observable<Cart>;
  userPoints: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  private cartBaseUri: string = this.globals.backendUri;
  constructor(private http: HttpClient, private globals: Globals) {}

  /**
   * Get the tickets in the cart of the specified user
   *
   * @param id the id of the user, whose cart should be fetched
   * @return an observable list of the tickets in the cart of the user
   */
  getCartTickets(id: number): Observable<Cart> {
    return this.http.get<Cart>(this.cartBaseUri + '/users/' + id + '/cart');
  }
  /**
   * Get the points in the cart of the specified user and update the userPoints subject
   *
   * @param id the id of the user, whose points should be fetched
   * @return an observable of the points in the cart of the user
   */
  getCartPoints(id: number): Observable<number> {
    this.cart = this.http.get<Cart>(
      this.cartBaseUri + '/users/' + id + '/cart'
    );
    this.cart.subscribe((data) => {
      this.userPoints.next(data.userPoints);
    });
    return this.userPoints;
  }
  /**
   * Remove a ticket from the specified user's cart
   *
   *  @param userId the id of the user, from whose cart the ticket should be deleted
   *  @param ticketId the id of the ticket that should be removed from the cart
   *  TODO: still a mock, implement backend
   */
  removeTicketFromCart(userId: number, ticketId: number): Observable<any> {
    return this.http.delete(
      this.cartBaseUri + '/users/' + userId + '/cart/tickets/' + ticketId
    );
  }

  buy(booking: Booking): Observable<OrderResponse> {
    return this.http.post<OrderResponse>(
      this.cartBaseUri + '/bookings',
      booking
    );
  }

  getCheckoutDetails(userId: number): Observable<CheckoutDetails> {
    return this.http.get<any>(
      this.cartBaseUri + '/users/' + userId + '/checkout-details'
    );
  }

  getMerchInfo(
    bookingMerchandises: BookingMerchandise[]
  ): Observable<Merchandise[]> {
    return this.http.post<Merchandise[]>(
      this.cartBaseUri + '/merch',
      bookingMerchandises
    );
  }
}
