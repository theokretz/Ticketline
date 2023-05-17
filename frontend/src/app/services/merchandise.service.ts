import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Observable} from 'rxjs';
import {Merchandise} from '../dtos/merchandise';

@Injectable({
  providedIn: 'root'
})
export class MerchandiseService {

  private merchandiseBaseUri: string = this.globals.backendUri +'/merch';

  constructor(
    private httpClient: HttpClient,
    private globals: Globals
  ) {
  }

  public getMerchandise(): Observable<Merchandise[]> {
    console.log(this.merchandiseBaseUri);
    return this.httpClient.get<Merchandise[]>(this.merchandiseBaseUri);
  }

}
