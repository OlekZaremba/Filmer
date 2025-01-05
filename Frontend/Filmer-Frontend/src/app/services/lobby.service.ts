import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Serwis LobbyService odpowiedzialny za obsługę lobby, w tym tworzenie, zamykanie,
 * zarządzanie uczestnikami oraz ustawieniami preferencji, a także rozpoczęcie gry.
 * @export
 * @class LobbyService
 */
@Injectable({
  providedIn: 'root'
})
export class LobbyService {
  /**
   * URL API do obsługi lobby.
   * @private
   * @type {string}
   */
  private apiUrl = 'http://localhost:8080/lobby/api';

  /**
   * Tworzy instancję LobbyService.
   * @param {HttpClient} http Serwis do wykonywania żądań HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Tworzy nowe lobby dla właściciela.
   * @param {number} ownerId ID właściciela lobby.
   * @returns {Observable<any>} Observable z odpowiedzią serwera.
   */
  createLobby(ownerId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${ownerId}/create`, {});
  }

  /**
   * Zamyka istniejące lobby.
   * @param {number} lobbyId ID lobby do zamknięcia.
   * @returns {Observable<void>} Observable z odpowiedzią serwera.
   */
  closeLobby(lobbyId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${lobbyId}/close`, {});
  }

  /**
   * Dodaje użytkownika do lobby na podstawie kodu lobby.
   * @param {string} lobbyCode Kod lobby.
   * @param {number} userId ID użytkownika do dodania.
   * @returns {Observable<void>} Observable z odpowiedzią serwera.
   */
  addUserToLobby(lobbyCode: string, userId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${lobbyCode}/addUser/${userId}`, {});
  }

  /**
   * Pobiera listę uczestników lobby na podstawie kodu lobby.
   * @param {string} lobbyCode Kod lobby.
   * @returns {Observable<any[]>} Observable zawierający listę uczestników.
   */
  getParticipants(lobbyCode: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/participants?lobbyCode=${lobbyCode}`);
  }

  /**
   * Zapisuje preferencje użytkownika dla lobby.
   * @param {string} lobbyCode Kod lobby.
   * @param {number} userId ID użytkownika.
   * @param {string} streamingPlatform Wybrana platforma streamingowa.
   * @param {string} genre Wybrany gatunek filmowy.
   * @param {string} type Wybrany typ filmu.
   * @returns {Observable<HttpResponse<any>>} Observable z odpowiedzią serwera w formacie HTTP.
   */
  savePreferences(
    lobbyCode: string,
    userId: number,
    streamingPlatform: string,
    genre: string,
    type: string
  ): Observable<HttpResponse<any>> {
    const body = { userId, streamingPlatform, genre, type };
    return this.http.post<any>(
      `${this.apiUrl}/${lobbyCode}/preferences`,
      body,
      { observe: 'response' }
    );
  }

  /**
   * Pobiera status gotowości uczestników w lobby.
   * @param {string} lobbyCode Kod lobby.
   * @returns {Observable<boolean>} Observable zawierający status gotowości.
   */
  getReadyStatus(lobbyCode: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${lobbyCode}/ready-status`);
  }

  /**
   * Rozpoczyna grę w danym lobby.
   * @param {string} lobbyCode Kod lobby.
   * @returns {Observable<void>} Observable z odpowiedzią serwera.
   */
  startGame(lobbyCode: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${lobbyCode}/start`, {});
  }

  /**
   * Sprawdza, czy gra w danym lobby została rozpoczęta.
   * @param {string} lobbyCode Kod lobby.
   * @returns {Observable<boolean>} Observable zawierający status rozpoczęcia gry.
   */
  isGameStarted(lobbyCode: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${lobbyCode}/is-started`);
  }
}

