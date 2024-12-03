import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {
  private apiUrl = 'http://localhost:8080/friends/api';

  constructor(private http: HttpClient) {}

  getFriends(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${userId}/list`);
  }

  uploadProfilePicture(userId: number, formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/${userId}/uploadProfilePicture`, formData);
  }

  getProfilePicture(userId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${userId}/profilePicture`, { responseType: 'blob' });
  }

  searchUsers(nick: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/search?nick=${nick}`);
  }

  addFriend(userId: number, friendId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/addFriend/${friendId}`, {});
  }

  sendInviteEmail(friendId: number, lobbyLink: string): Observable<void> {
    console.log({ friendId, lobbyLink });
    return this.http.post<void>(`${this.apiUrl}/sendInvite`, { friendId, lobbyLink });
  }
}
