import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Interfejs reprezentujący strukturę danych filmu w aplikacji.
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
   * Reżyser filmu.
   * @type {{ name: string }}
   */
  director: { name: string };

  /**
   * Studio, które wyprodukowało film.
   * @type {{ studioName: string }}
   */
  studio: { studioName: string };

  /**
   * Źródło filmu (np. platforma streamingowa).
   * @type {{ sourceName: string }}
   */
  source: { sourceName: string };

  /**
   * Gatunek filmu.
   * @type {{ genreName: string }}
   */
  genre: { genreName: string };

  /**
   * Typ filmu (np. fabularny, dokumentalny).
   * @type {{ filmType: string }}
   */
  type: { filmType: string };
}

/**
 * Serwis DrawService odpowiedzialny za obsługę losowania filmów,
 * głosowania oraz sprawdzania statusu głosowania w lobby.
 * @export
 * @class DrawService
 */
@Injectable({
  providedIn: 'root'
})
export class DrawService {
  /**
   * URL API do obsługi losowań i głosowań.
   * @private
   * @type {string}
   */
  private apiUrl = 'http://localhost:8080/api/draw';

  /**
   * Tworzy instancję DrawService.
   * @param {HttpClient} http Serwis do wykonywania żądań HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Pobiera listę filmów losowanych dla danego lobby.
   * @param {string} lobbyCode Kod lobby.
   * @returns {Observable<Film[]>} Observable zawierający listę filmów.
   */
  getDrawFilms(lobbyCode: string): Observable<Film[]> {
    return this.http.post<Film[]>(`${this.apiUrl}/${lobbyCode}/start`, {});
  }

  /**
   * Wysyła głos użytkownika na wybrany film w danym lobby.
   * @param {string} lobbyCode Kod lobby.
   * @param {number} filmId ID filmu, na który użytkownik głosuje.
   * @param {number} userId ID użytkownika oddającego głos.
   * @returns {Observable<any>} Observable z odpowiedzią serwera.
   */
  submitVote(lobbyCode: string, filmId: number, userId: number): Observable<any> {
    const params = { userId: userId.toString() };
    return this.http.post(`${this.apiUrl}/${lobbyCode}/vote/${filmId}`, {}, { params });
  }

  /**
   * Sprawdza status głosowania w danym lobby.
   * @param {string} lobbyCode Kod lobby.
   * @param {number} userId ID użytkownika sprawdzającego status.
   * @returns {Observable<boolean>} Observable z informacją, czy głosowanie zostało zakończone.
   */
  checkVotingStatus(lobbyCode: string, userId: number): Observable<boolean> {
    const params = { userId: userId.toString() };
    return this.http.get<boolean>(`http://localhost:8080/lobby/api/${lobbyCode}/status`, { params });
  }

  /**
   * Kończy głosowanie w danym lobby.
   * @param {string} lobbyCode Kod lobby.
   * @param {number} userId ID użytkownika kończącego głosowanie.
   * @returns {Observable<any>} Observable z odpowiedzią serwera.
   */
  finishVoting(lobbyCode: string, userId: number): Observable<any> {
    return this.http.post(`http://localhost:8080/lobby/api/${lobbyCode}/finish-voting`, { userId });
  }
}
