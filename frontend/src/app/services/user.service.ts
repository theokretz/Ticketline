import { Injectable } from '@angular/core';
import { Globals } from '../global/globals';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../dtos/reservation';
import { SimpleTicket } from '../dtos/ticket';

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
}
