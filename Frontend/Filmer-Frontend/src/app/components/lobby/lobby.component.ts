import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {FriendsService} from '../../services/friends.service';

@Component({
  selector: 'app-lobby',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './lobby.component.html',
  styleUrl: './lobby.component.css'
})
export class LobbyComponent implements OnInit {
  userName: string = '';
  email: string = '';
  friends: any[] = [];

  constructor(private friendsService: FriendsService) {}
  ngOnInit(): void {
    const email = localStorage.getItem('email');
    const nick = localStorage.getItem('nick');
    const userId = localStorage.getItem('userId');

    if (email && nick) {
      this.userName = nick;
      this.email = email;

      if (userId) {
        this.loadFriends(+userId);
        this.loadProfilePicture(+userId);
      } else {
        console.error('ID użytkownika nie jest dostępne w localStorage.');
      }
    } else {
      console.error('E-mail lub nick użytkownika nie są dostępne w localStorage.');
    }
  }
  loadFriends(userId: number): void {
    this.friendsService.getFriends(userId).subscribe({
      next: (friends) => {
        this.friends = friends;

        // Dla każdego znajomego pobierz jego zdjęcie profilowe
        this.friends.forEach(friend => {
          this.loadFriendPicture(friend);
        });
      },
      error: (err) => {
        console.error('Nie udało się pobrać listy znajomych:', err);
      },
    });
  }

  loadFriendPicture(friend: any): void {
    this.friendsService.getProfilePicture(friend.id).subscribe({
      next: (blob) => {
        const objectURL = URL.createObjectURL(blob);
        friend.avatar = objectURL;
      },
      error: (err) => {
        console.error(`Nie udało się pobrać zdjęcia dla użytkownika ${friend.id}:`, err);
        friend.avatar = 'assets/images/user.png';
      },
    });
  }

  loadProfilePicture(userId: number): void {
    this.friendsService.getProfilePicture(userId).subscribe({
      next: (blob) => {
        const objectURL = URL.createObjectURL(blob);
        const imgElement = document.querySelector('.profile-picture') as HTMLImageElement;
        if (imgElement) {
          imgElement.src = objectURL;
        }
      },
      error: (err) => {
        console.error('Błąd podczas ładowania zdjęcia profilowego:', err);
      },
    });
  }

}
