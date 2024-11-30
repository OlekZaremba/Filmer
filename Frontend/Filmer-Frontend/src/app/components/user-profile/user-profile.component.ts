import { Component, OnInit } from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {FriendsService} from '../../services/friends.service';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  userName: string = '';
  email: string = '';
  friends: any[] = [];

  constructor(private friendsService: FriendsService) {
  }

  ngOnInit(): void {
    const email = localStorage.getItem('email'); // Pobierz email
    const nick = localStorage.getItem('nick'); // Pobierz nick
    const userId = localStorage.getItem('userId'); // Pobierz userId

    if (email && nick) {
      this.userName = nick; // Ustaw nick
      this.email = email; // Ustaw email

      if (userId) {
        this.friendsService.getFriends(+userId).subscribe({
          next: (friends) => {
            this.friends = friends;
          },
          error: (err) => {
            console.error('Nie udało się pobrać listy znajomych:', err);
          },
        });
      } else {
        console.error('ID użytkownika nie jest dostępne w localStorage.');
      }
    } else {
      console.error('E-mail lub nick użytkownika nie są dostępne w localStorage.');
    }
  }
}
