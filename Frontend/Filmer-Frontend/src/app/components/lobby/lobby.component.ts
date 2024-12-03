import {Component, OnInit} from '@angular/core';
import {FriendsService} from '../../services/friends.service';
import {LobbyService} from '../../services/lobby.service';
import {ActivatedRoute} from '@angular/router';
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
  lobbyCode: string = '';

  constructor(private friendsService: FriendsService,
              private lobbyService: LobbyService,
              private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.lobbyCode = this.route.snapshot.paramMap.get('lobbyCode') || '';

    if (this.lobbyCode) {
      this.updateParticipants();
    }

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
          this.lobbyCode = lobby.lobbyCode;
          this.lobbyLink = `http://localhost:4200/lobby/${this.lobbyCode}`;
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
      this.friendsService.sendInviteEmail(friend.id, this.lobbyLink).subscribe({
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
    if (this.lobbyCode) {
      this.lobbyService.getParticipants(this.lobbyCode).subscribe({
        next: (participants) => {
          this.participants = participants;
        },
        error: (err) => {
          console.error('Nie udało się pobrać listy uczestników:', err);
        },
      });
    }
  }
}
