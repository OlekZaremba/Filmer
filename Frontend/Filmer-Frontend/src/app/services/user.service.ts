import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/user.model';

/**
 * Serwis UserService odpowiedzialny za zarządzanie informacjami o użytkownikach,
 * w tym pobieranie szczegółów użytkownika na podstawie adresu e-mail.
 * @export
 * @class UserService
 */
@Injectable({
  providedIn: 'root',
})
export class UserService {
  /**
   * URL API do obsługi użytkowników.
   * @private
   * @type {string}
   */
  private apiUrl = 'http://localhost:8080/api/auth';

  /**
   * Tworzy instancję UserService.
   * @param {HttpClient} http Serwis do wykonywania żądań HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Pobiera szczegóły użytkownika na podstawie adresu e-mail.
   * @param {string} email Adres e-mail użytkownika.
   * @returns {Observable<User>} Observable zawierający szczegóły użytkownika.
   */
  getUserDetails(email: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/details?email=${email}`);
  }
}
