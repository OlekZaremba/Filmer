import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';

/**
 * Serwis AuthService odpowiedzialny za uwierzytelnianie użytkowników,
 * obsługę logowania, rejestracji oraz wylogowywania.
 * Przechowuje informacje o aktualnym statusie zalogowania użytkownika.
 * @export
 * @class AuthService
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  /**
   * URL API do obsługi uwierzytelniania.
   * @private
   * @type {string}
   */
  private apiUrl = 'http://localhost:8080/api/auth';

  /**
   * BehaviorSubject przechowujący informacje o statusie zalogowania użytkownika.
   * @private
   * @type {BehaviorSubject<boolean>}
   */
  private loggedIn = new BehaviorSubject<boolean>(false);

  /**
   * Observable emitujące aktualny status zalogowania użytkownika.
   * @type {Observable<boolean>}
   */
  public isLoggedIn$ = this.loggedIn.asObservable();

  /**
   * Tworzy instancję AuthService.
   * Sprawdza, czy istnieje token w localStorage i ustawia status zalogowania.
   * @param {HttpClient} http Serwis do wykonywania żądań HTTP.
   * @param {Router} router Angularowy router do nawigacji.
   */
  constructor(private http: HttpClient, private router: Router) {
    const token = localStorage.getItem('authToken');
    this.loggedIn.next(!!token);
  }

  /**
   * Loguje użytkownika, wysyłając dane uwierzytelniające na serwer.
   * Po poprawnym logowaniu zapisuje token i dane użytkownika w localStorage oraz aktualizuje status zalogowania.
   * @param {string} email E-mail użytkownika.
   * @param {string} password Hasło użytkownika.
   * @param {string} captchaResponse Odpowiedź CAPTCHA.
   * @returns {Observable<any>} Observable z odpowiedzią serwera.
   */
  login(email: string, password: string, captchaResponse: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/login`, { email, password, captchaResponse }).pipe(
      tap((response: any) => {
        console.log('Odpowiedź serwera:', response);
        if (response && response.token) {
          localStorage.setItem('authToken', response.token);
          localStorage.setItem('nick', response.nick);
          localStorage.setItem('email', response.email);
          localStorage.setItem('userId', response.userId);
          this.loggedIn.next(true);
          console.log('Zalogowano użytkownika. Nick:', response.nick, 'Email:', response.email);
        }
      })
    );
  }

  /**
   * Wylogowuje użytkownika, usuwając dane z localStorage i aktualizując status zalogowania.
   * Następnie przekierowuje użytkownika na stronę główną.
   */
  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('email');
    localStorage.removeItem('nick');
    localStorage.removeItem('userId');
    this.loggedIn.next(false);
    this.router.navigate(['/']);
  }

  /**
   * Rejestruje nowego użytkownika, wysyłając dane rejestracyjne na serwer.
   * @param {string} username Nick użytkownika.
   * @param {string} email E-mail użytkownika.
   * @param {string} password Hasło użytkownika.
   * @param {string} confirmPassword Potwierdzenie hasła.
   * @returns {Observable<any>} Observable z odpowiedzią serwera.
   */
  register(username: string, email: string, password: string, confirmPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { username, email, password, confirmPassword });
  }
}
