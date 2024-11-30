import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {
  private apiUrl = 'http://localhost:8080/friends/api';

  constructor(private http: HttpClient) {}

  getFriends(userId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${userId}/list`);
  }

  searchUsers(nick: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/search?nick=${nick}`);
  }

  addFriend(userId: number, friendId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/addFriend/${friendId}`, {});
  }
}
