import { Component, OnInit } from '@angular/core';
import { FriendsService } from '../../services/friends.service';
import { Friend } from '../models/friend.model';

/**
 * Komponent odpowiedzialny za wyświetlanie listy znajomych użytkownika.
 * @export
 * @class FriendsListComponent
 * @implements {OnInit}
 */
@Component({
  selector: 'app-friends-list',
  standalone: true,
  imports: [],
  templateUrl: './friends-list.component.html',
  styleUrl: './friends-list.component.css'
})
export class FriendsListComponent implements OnInit {
  /**
   * Tablica przechowująca listę znajomych użytkownika.
   * @type {Friend[]}
   */
  friends: Friend[] = [];

  /**
   * Tworzy instancję komponentu.
   * @param {FriendsService} friendsService - Usługa do obsługi danych znajomych.
   */
  constructor(private friendsService: FriendsService) {}

  /**
   * Inicjalizuje komponent, pobierając listę znajomych dla użytkownika.
   * @memberof FriendsListComponent
   */
  ngOnInit(): void {
    const userId = 2; // Tymczasowy identyfikator użytkownika

    this.friendsService.getFriendsList(userId).subscribe({
      next: (data) => {
        this.friends = data;
      },
      error: (error) => {
        console.error('Nie udało się pobrać listy znajomych', error);
      }
    });
  }
}
