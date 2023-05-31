import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Artist} from '../dtos/artist';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Globals} from '../global/globals';
import {Event} from '../dtos/event';


@Injectable({
  providedIn: 'root'
})
export class EventService {

  private baseUri: string = this.globals.backendUri + '/search';

  constructor(
    private http: HttpClient,
    private globals: Globals
  ) {
  }

  /**
   * Get all artists with name like the specified one
   *
   * @param artistName the sequence of letters of which the artists' names should consist
   * @return an observable list of the found artists
   */
  searchArtistsByName(artistName: string): Observable<Artist[]> {
    const params = new HttpParams()
      .set('name', artistName);
    return this.http.get<Artist[]>(this.baseUri + '/artists', { params });
  }

  /**
   * Get the events of the specified artist
   *
   * @param artistName the name of the artist, whose events should be fetched
   * @return an observable list of the found events
   */
  searchEventsByArtistName(artistId: number, artistName: string): Observable<Event[]> {
    const params = new HttpParams()
      .set('id', artistId)
      .set('name', artistName);
    return this.http.get<Event[]>(this.baseUri + '/events-by-artist', { params });
  }
}
