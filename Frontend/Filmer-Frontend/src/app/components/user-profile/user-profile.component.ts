import { Component, OnInit } from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import {FriendsService} from '../../services/friends.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  userName: string = '';
  email: string = '';
  friends: any[] = [];
  searchTerm: string = '';
  searchResults: any[] = [];

  constructor(private friendsService: FriendsService) {
  }

  ngOnInit(): void {
    const email = localStorage.getItem('email');
    const nick = localStorage.getItem('nick');
    const userId = localStorage.getItem('userId');

    if (email && nick) {
      this.userName = nick;
      this.email = email;

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

  searchUsers(): void {
    if (this.searchTerm.trim()) {
      this.friendsService.searchUsers(this.searchTerm).subscribe({
        next: (results) => {
          console.log('Wyniki wyszukiwania:', results);
          this.searchResults = results;
        },
        error: (err) => {
          console.error('Błąd podczas wyszukiwania użytkowników:', err);
        },
      });
    } else {
      this.searchResults = [];
    }
  }


  addFriend(friendId: number): void {
    console.log('Próba dodania znajomego, friendId:', friendId);
    const userId = localStorage.getItem('userId');

    if (userId) {
      this.friendsService.addFriend(+userId, friendId).subscribe({
        next: () => {
          alert('Użytkownik został dodany do znajomych.');
          this.loadFriends(+userId);
          this.searchResults = this.searchResults.filter(user => user.id !== friendId);
        },
        error: (err) => {
          if (err.status === 400) {
            alert('Nie można dodać znajomego: ' + err.error.message);
          } else {
            console.error('Błąd podczas dodawania znajomego:', err);
          }
        },
      });
    } else {
      console.error('Brak userId w localStorage.');
    }
  }



  loadFriends(userId: number): void {
    this.friendsService.getFriends(userId).subscribe({
      next: (friends) => {
        console.log('Odświeżona lista znajomych:', friends);
        this.friends = friends;
      },
      error: (err) => {
        console.error('Nie udało się pobrać listy znajomych:', err);
      },
    });
  }
}
