import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ResultsService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  getResults(lobbyCode: string): Observable<{ [key: number]: any[] }> {
    return this.http.get<{ [key: number]: any[] }>(`${this.apiUrl}/results/${lobbyCode}`);
  }

  sendResultsEmail(lobbyCode: string, email: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/results/${lobbyCode}/sendEmail`, null, {
      params: { email }
    });
  }

}
