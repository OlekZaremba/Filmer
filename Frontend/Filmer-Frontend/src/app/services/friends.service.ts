import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Serwis FriendsService odpowiedzialny za obsługę listy znajomych,
 * zarządzanie zdjęciem profilowym, wyszukiwanie użytkowników oraz wysyłanie zaproszeń.
 * @export
 * @class FriendsService
 */
@Injectable({
  providedIn: 'root'
})
export class FriendsService {
  /**
   * URL API do obsługi znajomych.
   * @private
   * @type {string}
   */
  private apiUrl = 'http://localhost:8080/friends/api';

  /**
   * Tworzy instancję FriendsService.
   * @param {HttpClient} http Serwis do wykonywania żądań HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Pobiera listę znajomych dla danego użytkownika.
   * @param {number} userId ID użytkownika.
   * @returns {Observable<any[]>} Observable zawierający listę znajomych.
   */
  getFriends(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${userId}/list`);
  }

  /**
   * Przesyła zdjęcie profilowe użytkownika na serwer.
   * @param {number} userId ID użytkownika.
   * @param {FormData} formData Obiekt FormData zawierający zdjęcie profilowe.
   * @returns {Observable<any>} Observable z odpowiedzią serwera.
   */
  uploadProfilePicture(userId: number, formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/${userId}/uploadProfilePicture`, formData);
  }

  /**
   * Pobiera zdjęcie profilowe użytkownika z serwera.
   * @param {number} userId ID użytkownika.
   * @returns {Observable<Blob>} Observable zawierający zdjęcie profilowe jako Blob.
   */
  getProfilePicture(userId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${userId}/profilePicture`, { responseType: 'blob' });
  }

  /**
   * Wyszukuje użytkowników na podstawie nicku.
   * @param {string} nick Nick użytkownika do wyszukania.
   * @returns {Observable<any[]>} Observable zawierający listę pasujących użytkowników.
   */
  searchUsers(nick: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/search?nick=${nick}`);
  }

  /**
   * Dodaje użytkownika do listy znajomych.
   * @param {number} userId ID użytkownika dodającego znajomego.
   * @param {number} friendId ID użytkownika, który ma zostać dodany jako znajomy.
   * @returns {Observable<void>} Observable z odpowiedzią serwera.
   */
  addFriend(userId: number, friendId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/addFriend/${friendId}`, {});
  }

  /**
   * Wysyła e-mail z zaproszeniem do lobby do znajomego.
   * @param {number} friendId ID znajomego, do którego ma zostać wysłane zaproszenie.
   * @param {string} lobbyLink Link do lobby.
   * @returns {Observable<void>} Observable z odpowiedzią serwera.
   */
  sendInviteEmail(friendId: number, lobbyLink: string): Observable<void> {
    console.log({ friendId, lobbyLink });
    return this.http.post<void>(`${this.apiUrl}/sendInvite`, { friendId, lobbyLink });
  }
}
