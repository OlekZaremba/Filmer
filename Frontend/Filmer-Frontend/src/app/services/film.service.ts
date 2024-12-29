import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FilmService {
  private apiUrl = 'http://localhost:8080/api/library/films';

  constructor(private http: HttpClient) {}

  getFilmById(id: number): Observable<Film> {
    return this.http.get<Film>(`${this.apiUrl}/${id}`);
  }

  getAllFilms(): Observable<Film[]> {
    return this.http.get<Film[]>(this.apiUrl);
  }

  getFilmsByGenre(genre: string): Observable<Film[]> {
    return this.http.get<Film[]>(`${this.apiUrl}/filter?genre=${genre}`);
  }

  getFilmsByName(name: string): Observable<Film[]> {
    return this.http.get<Film[]>(`${this.apiUrl}/search?name=${name}`);
  }

  getRating(filmId: number, userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/watched-movies/${filmId}/rating?userId=${userId}`);
  }

  setRating(filmId: number, userId: number, rating: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/watched-movies/${filmId}/rating?userId=${userId}&rating=${rating}`, {});
  }
}

export interface Film {
  idFilm: number;
  filmName: string;
  filmDesc: string;
  filmImage: string;
  director: { id: number; name: string };
  studio: { id: number; studioName: string };
  type: { id: number; name: string };
  source: { id: number; sourceName: string };
  genre: { id: number; name: string };
}
