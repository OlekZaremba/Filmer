import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Film {
  idFilm: number;
  filmName: string;
  filmDesc: string;
  director: { name: string };
  studio: { studioName: string };
  source: { sourceName: string };
  genre: { genreName: string };
  type: { filmType: string };
}

@Injectable({
  providedIn: 'root'
})
export class DrawService {
  private apiUrl = 'http://localhost:8080/api/draw';

  constructor(private http: HttpClient) {}

  getDrawFilms(lobbyCode: string): Observable<Film[]> {
    return this.http.post<Film[]>(`${this.apiUrl}/${lobbyCode}/start`, {});
  }

  submitVote(lobbyCode: string, filmId: number, userId: number): Observable<any> {
    const params = { userId: userId.toString() };
    return this.http.post(`${this.apiUrl}/${lobbyCode}/vote/${filmId}`, {}, { params });
  }

}
