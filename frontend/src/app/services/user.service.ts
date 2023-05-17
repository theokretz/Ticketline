import { Injectable } from '@angular/core';
import { Globals } from '../global/globals';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../dtos/reservation';
import { SimpleTicket } from '../dtos/ticket';
import {Order} from '../dtos/order';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private userBaseUri: string = this.globals.backendUri + '/users';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  public addTicketsToCart(
    id: number,
    ticketIds: SimpleTicket[]
  ): Observable<Reservation> {
    return this.httpClient.post<any>(
      this.userBaseUri + '/' + id + '/cart/tickets',
      ticketIds
    );
  }

  /**
   * Get all orders of the specified user
   *
   * @param id the id of the user whose orders should be fetched
   * @return an Observable List of all orders of the user
   */
  public getOrders(id: number): Observable<Order[]> {
    return this.httpClient.get<Order[]>(this.userBaseUri + '/' + id + '/order-history');
  }
}
