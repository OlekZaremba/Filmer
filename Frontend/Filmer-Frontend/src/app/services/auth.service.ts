import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth';
  private loggedIn = new BehaviorSubject<boolean>(false);
  public isLoggedIn$ = this.loggedIn.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    const token = localStorage.getItem('authToken');
    this.loggedIn.next(!!token);
  }

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

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('email');
    localStorage.removeItem('nick');
    localStorage.removeItem('userId');
    this.loggedIn.next(false);
    this.router.navigate(['/']);
  }

  register(username: string, email: string, password: string, confirmPassword: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/register`, { username, email, password, confirmPassword });
  }
}
