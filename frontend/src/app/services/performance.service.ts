import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Globals } from '../global/globals';
import { Performance } from '../dtos/performance';

@Injectable({
  providedIn: 'root',
})
export class PerformanceService {
  private performanceBaseUri: string =
    this.globals.backendUri + '/performances';

  constructor(private httpClient: HttpClient, private globals: Globals) {}

  public getPerformanceById(id: number): Observable<Performance> {
    return this.httpClient.get<Performance>(this.performanceBaseUri + '/' + id);
  }
}
