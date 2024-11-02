import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

interface User {
  nick: string;
  email: string;
}
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api/auth'
  constructor(private http: HttpClient) {}

  getUserDetails(email: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/details`, { params: { email } });
  }
}
