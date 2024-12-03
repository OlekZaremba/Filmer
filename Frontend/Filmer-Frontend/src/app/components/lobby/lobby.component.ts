import {Component, OnInit} from '@angular/core';
import {FriendsService} from '../../services/friends.service';
import {LobbyService} from '../../services/lobby.service';
import {NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-lobby',
  standalone: true,
  templateUrl: './lobby.component.html',
  imports: [NgForOf, NgIf],
  styleUrl: './lobby.component.css'
})
export class LobbyComponent implements OnInit {
  userName: string = '';
  email: string = '';
  friends: any[] = [];
  lobbyLink: string = '';
  participants: any[] = [];

  constructor(private friendsService: FriendsService, private lobbyService: LobbyService) {}

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

  generateLink(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.lobbyService.createLobby(+userId).subscribe({
        next: (lobby) => {
          this.lobbyLink = `http://localhost:4200/lobby/${lobby.lobbyCode}`;
        },
        error: (err) => {
          console.error('Nie udało się utworzyć lobby:', err);
        },
      });
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

  sendInvite(friend: any): void {
    if (this.lobbyLink) {
      this.friendsService.sendInviteEmail(friend.email, this.lobbyLink).subscribe({
        next: () => {
          console.log(`Zaproszenie wysłane do ${friend.nick}`);
        },
        error: (err) => {
          console.error('Nie udało się wysłać zaproszenia:', err);
        },
      });
    } else {
      console.error('Link do lobby nie został jeszcze wygenerowany.');
    }
  }

  updateParticipants(): void {
    const lobbyCode = this.extractLobbyCodeFromUrl();
    if (lobbyCode) {
      this.lobbyService.getParticipants(lobbyCode).subscribe({
        next: (participants) => {
          this.participants = participants;
        },
        error: (err) => {
          console.error('Nie udało się pobrać listy uczestników:', err);
        },
      });
    }
  }

  extractLobbyCodeFromUrl(): string | null {
    const url = window.location.href;
    const parts = url.split('/');
    return parts[parts.length - 1] || null;
  }

  // ngAfterViewInit(): void {
  //   this.updateParticipants();
  //   setInterval(() => {
  //     this.updateParticipants();
  //   }, 5000);
  // }
}
