import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Globals } from '../global/globals';
import { Observable } from 'rxjs';
import { OrderPage, OrderResponse } from '../dtos/order';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private orderBaseUri: string = this.globals.backendUri + '/orders';

  constructor(private http: HttpClient, private globals: Globals) {}

  /**
   * Get the order with the specified id
   *
   * @param id the id of the order that should be fetched
   * @returns an observable of the order
   */
  getOrderById(id: number): Observable<OrderPage> {
    return this.http.get<OrderPage>(this.orderBaseUri + '/' + id);
  }

  cancelOrder(id: number): Observable<any> {
    return this.http.delete(this.orderBaseUri + '/' + id);
  }

  cancelItems(
    id: number,
    tickets: number[],
    merchandise: number[]
  ): Observable<any> {
    const params: { tickets?: string; merchandise?: string } = {};

    if (tickets.length > 0) {
      params.tickets = tickets.join(',');
    }

    if (merchandise.length > 0) {
      params.merchandise = merchandise.join(',');
    }
    const options = { params };
    return this.http.delete<OrderResponse>(
      this.orderBaseUri + '/' + id + '/items',
      options
    );
  }
}
