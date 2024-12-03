import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LobbyService {
  private apiUrl = 'http://localhost:8080/lobby/api';

  constructor(private http: HttpClient) {}

  createLobby(ownerId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${ownerId}/create`, {});
  }

  getParticipants(lobbyCode: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${lobbyCode}/participants`);
  }

  addUserToLobby(lobbyCode: string, userId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${lobbyCode}/addUser/${userId}`, {});
  }
}
