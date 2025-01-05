import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

/**
 * Serwis FilmService odpowiedzialny za zarządzanie danymi o filmach,
 * w tym ich pobieranie, filtrowanie, wyszukiwanie oraz obsługę ocen użytkowników.
 * @export
 * @class FilmService
 */
@Injectable({
  providedIn: 'root',
})
export class FilmService {
  /**
   * URL API do obsługi filmów.
   * @private
   * @type {string}
   */
  private apiUrl = 'http://localhost:8080/api/library/films';

  /**
   * Tworzy instancję FilmService.
   * @param {HttpClient} http Serwis do wykonywania żądań HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Pobiera szczegóły filmu na podstawie jego ID.
   * @param {number} id ID filmu.
   * @returns {Observable<Film>} Observable zawierający szczegóły filmu.
   */
  getFilmById(id: number): Observable<Film> {
    return this.http.get<Film>(`${this.apiUrl}/${id}`);
  }

  /**
   * Pobiera listę wszystkich dostępnych filmów.
   * @returns {Observable<Film[]>} Observable zawierający listę filmów.
   */
  getAllFilms(): Observable<Film[]> {
    return this.http.get<Film[]>(this.apiUrl);
  }

  /**
   * Pobiera filmy na podstawie gatunku.
   * @param {string} genre Gatunek filmowy.
   * @returns {Observable<Film[]>} Observable zawierający listę filmów danego gatunku.
   */
  getFilmsByGenre(genre: string): Observable<Film[]> {
    return this.http.get<Film[]>(`${this.apiUrl}/filter?genre=${genre}`);
  }

  /**
   * Wyszukuje filmy na podstawie nazwy.
   * @param {string} name Nazwa filmu.
   * @returns {Observable<Film[]>} Observable zawierający listę pasujących filmów.
   */
  getFilmsByName(name: string): Observable<Film[]> {
    return this.http.get<Film[]>(`${this.apiUrl}/search?name=${name}`);
  }

  /**
   * Pobiera ocenę użytkownika dla danego filmu.
   * @param {number} filmId ID filmu.
   * @param {number} userId ID użytkownika.
   * @returns {Observable<number>} Observable zawierający ocenę użytkownika.
   */
  getRating(filmId: number, userId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/watched-movies/${filmId}/rating?userId=${userId}`);
  }

  /**
   * Ustawia ocenę użytkownika dla danego filmu.
   * @param {number} filmId ID filmu.
   * @param {number} userId ID użytkownika.
   * @param {number} rating Ocena użytkownika (1-10).
   * @returns {Observable<any>} Observable z odpowiedzią serwera.
   */
  setRating(filmId: number, userId: number, rating: number): Observable<any> {
    return this.http.post<any>(
      `${this.apiUrl}/watched-movies/${filmId}/rating?userId=${userId}&rating=${rating}`,
      {}
    );
  }
}

/**
 * Interfejs Film reprezentujący dane o filmie.
 * @export
 * @interface Film
 */
export interface Film {
  /**
   * Unikalny identyfikator filmu.
   * @type {number}
   */
  idFilm: number;

  /**
   * Nazwa filmu.
   * @type {string}
   */
  filmName: string;

  /**
   * Opis filmu.
   * @type {string}
   */
  filmDesc: string;

  /**
   * URL obrazu filmu.
   * @type {string}
   */
  filmImage: string;

  /**
   * Informacje o reżyserze filmu.
   * @type {{ id: number; name: string }}
   */
  director: { id: number; name: string };

  /**
   * Informacje o studiu filmowym.
   * @type {{ id: number; studioName: string }}
   */
  studio: { id: number; studioName: string };

  /**
   * Typ filmu (np. fabularny, dokumentalny).
   * @type {{ id: number; name: string }}
   */
  type: { id: number; name: string };

  /**
   * Źródło filmu (np. platforma streamingowa).
   * @type {{ id: number; sourceName: string }}
   */
  source: { id: number; sourceName: string };

  /**
   * Gatunek filmu.
   * @type {{ id: number; name: string }}
   */
  genre: { id: number; name: string };
}
