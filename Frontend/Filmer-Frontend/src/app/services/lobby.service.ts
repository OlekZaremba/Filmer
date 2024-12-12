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

  closeLobby(lobbyId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${lobbyId}/close`, {});
  }

  addUserToLobby(lobbyCode: string, userId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${lobbyCode}/addUser/${userId}`, {});
  }

  getParticipants(lobbyCode: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/participants?lobbyCode=${lobbyCode}`);
  }

  savePreferences(lobbyCode: string, userId: number, streamingPlatform: string, genre: string, type: string): Observable<void> {
    const body = { userId, streamingPlatform, genre, type };
    return this.http.post<void>(`${this.apiUrl}/${lobbyCode}/preferences`, body);
  }

  getReadyStatus(lobbyCode: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${lobbyCode}/ready-status`);
  }

  startGame(lobbyCode: string): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${lobbyCode}/start`, {});
  }

  isGameStarted(lobbyCode: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/${lobbyCode}/is-started`);
  }

}
