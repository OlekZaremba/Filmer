import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Serwis ResultsService odpowiedzialny za obsługę wyników głosowania,
 * w tym pobieranie wyników oraz wysyłanie ich na adres e-mail.
 * @export
 * @class ResultsService
 */
@Injectable({
  providedIn: 'root',
})
export class ResultsService {
  /**
   * URL API do obsługi wyników.
   * @private
   * @type {string}
   */
  private apiUrl = 'http://localhost:8080/api';

  /**
   * Tworzy instancję ResultsService.
   * @param {HttpClient} http Serwis do wykonywania żądań HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Pobiera wyniki głosowania dla danego lobby.
   * @param {string} lobbyCode Kod lobby.
   * @returns {Observable<{ [key: number]: any[] }>} Observable zawierający wyniki głosowania.
   */
  getResults(lobbyCode: string): Observable<{ [key: number]: any[] }> {
    return this.http.get<{ [key: number]: any[] }>(`${this.apiUrl}/results/${lobbyCode}`);
  }

  /**
   * Wysyła wyniki głosowania na podany adres e-mail.
   * @param {string} lobbyCode Kod lobby.
   * @param {string} email Adres e-mail, na który mają zostać wysłane wyniki.
   * @returns {Observable<void>} Observable z odpowiedzią serwera.
   */
  sendResultsEmail(lobbyCode: string, email: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/results/${lobbyCode}/sendEmail`, null, {
      params: { email }
    });
  }
}
