import { Injectable } from '@angular/core';
import { Globals } from '../global/globals';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../dtos/reservation';
import { SimpleTicket } from '../dtos/ticket';
import { Order } from '../dtos/order';
import { CheckoutLocation, Location } from '../dtos/location';
import { CheckoutPaymentDetail, PaymentDetail } from '../dtos/payment-detail';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private userBaseUri: string = this.globals.backendUri + '/users/';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  addTicketsToCart(
    id: number,
    ticketIds: SimpleTicket[]
  ): Observable<Reservation> {
    return this.httpClient.post<any>(
      this.userBaseUri + id + '/cart/tickets',
      ticketIds
    );
  }

  /**
   * Get all orders of the specified user
   *
   * @param id the id of the user whose orders should be fetched
   * @return an Observable List of all orders of the user
   */
  getOrders(id: number): Observable<Order[]> {
    return this.httpClient.get<Order[]>(
      this.userBaseUri + id + '/order-history'
    );
  }

  /**
   * Create a location for a user
   *
   * @param id the user id
   * @param location the location to create
   * @returns the created location
   */
  createLocation(id: number, location: Location): Observable<CheckoutLocation> {
    return this.httpClient.post<any>(
      this.userBaseUri + id + '/locations',
      location
    );
  }

  /**
   * Create a payment detail for a user
   *
   * @param id the user id
   * @param paymentDetail the payment detail to create
   * @returns the created payment detail
   */
  createPaymentDetail(
    id: number,
    paymentDetail: PaymentDetail
  ): Observable<CheckoutPaymentDetail> {
    return this.httpClient.post<any>(
      this.userBaseUri + id + '/payment-details',
      paymentDetail
    );
  }
}
