import { Component, OnInit, OnDestroy } from '@angular/core';
import { FriendsService } from '../../services/friends.service';
import { LobbyService } from '../../services/lobby.service';
import { NgForOf, NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { interval, Subscription } from 'rxjs';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-lobby',
  standalone: true,
  templateUrl: './lobby.component.html',
  imports: [NgForOf, NgIf, FormsModule],
  styleUrls: ['./lobby.component.css'],
})
export class LobbyComponent implements OnInit, OnDestroy {
  userName: string = '';
  email: string = '';
  friends: any[] = [];
  lobbyLink: string = '';
  participants: any[] = [];
  canSendInvites: boolean = false;
  isGeneratingLobby: boolean = false;
  selectedPlatform: string = '';
  selectedGenre: string = '';
  selectedType: string = '';

  private participantSubscription: Subscription | undefined;

  constructor(
    private friendsService: FriendsService,
    private lobbyService: LobbyService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const email = localStorage.getItem('email');
    const nick = localStorage.getItem('nick');
    const userId = localStorage.getItem('userId');
    const storedLobbyLink = localStorage.getItem('lobbyLink');

    if (storedLobbyLink) {
      this.lobbyLink = storedLobbyLink;
      this.canSendInvites = true;
    }

    if (email && nick) {
      this.userName = nick;
      this.email = email;

      if (userId) {
        this.loadFriends(+userId);
        this.loadProfilePicture(+userId);
        this.addUserToLobbyIfJoining(+userId);
      } else {
        console.error('ID użytkownika nie jest dostępne w localStorage.');
      }
    } else {
      console.error('E-mail lub nick użytkownika nie są dostępne w localStorage.');
    }

    this.participantSubscription = interval(5000).subscribe(() => {
      this.loadParticipants();
    });

    this.loadParticipants();
  }

  ngOnDestroy(): void {
    if (this.participantSubscription) {
      this.participantSubscription.unsubscribe();
    }
  }

  generateLink(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.isGeneratingLobby = true;
      this.lobbyService.createLobby(+userId).subscribe({
        next: (lobby) => {
          this.lobbyLink = `http://localhost:4200/lobby/${lobby.lobbyCode}`;
          localStorage.setItem('lobbyLink', this.lobbyLink);
          this.canSendInvites = true;
          this.isGeneratingLobby = false;
          this.router.navigate([`/lobby/${lobby.lobbyCode}`]);
        },
        error: (err) => {
          console.error('Nie udało się utworzyć lobby:', err);
          this.isGeneratingLobby = false;
        },
      });
    }
  }

  addUserToLobbyIfJoining(userId: number): void {
    const lobbyCode = this.extractLobbyCodeFromUrl();
    if (lobbyCode) {
      this.lobbyService.addUserToLobby(lobbyCode, userId).subscribe({
        next: () => {
          console.log('Użytkownik został dodany do lobby.');
          this.loadParticipants();
        },
        error: (err) => {
          console.error('Nie udało się dodać użytkownika do lobby:', err);
        },
      });
    }
  }

  startLobby(): void {
    if (this.lobbyLink) {
      const lobbyCode = this.lobbyLink.split('/').pop();
      if (lobbyCode) {
        this.router.navigate([`/lobby/${lobbyCode}`]);
      }
    }
  }

  isCreatingNewLobbyDisabled(): boolean {
    return this.isGeneratingLobby || !!this.lobbyLink;
  }

  loadFriends(userId: number): void {
    this.friendsService.getFriends(userId).subscribe({
      next: (friends) => {
        this.friends = friends;
        this.friends.forEach((friend) => {
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
    if (this.canSendInvites && this.lobbyLink) {
      this.friendsService.sendInviteEmail(friend.id, this.lobbyLink).subscribe({
        next: () => {
          console.log(`Zaproszenie wysłane do ${friend.nick}`);
        },
        error: (err) => {
          console.error('Nie udało się wysłać zaproszenia:', err);
        },
      });
    } else {
      console.error('Link do lobby nie został jeszcze wygenerowany lub wysyłanie zaproszeń jest zablokowane.');
    }
  }

  loadParticipants(): void {
    const lobbyCode = this.extractLobbyCodeFromUrl();
    if (lobbyCode) {
      this.lobbyService.getParticipants(lobbyCode).subscribe({
        next: (participants) => {
          console.log('Pobrani uczestnicy:', participants);
          this.participants = participants;
        },
        error: (err) => {
          console.error('Nie udało się pobrać listy uczestników:', err);
        },
      });
    }
  }

  savePreferences(): void {
    const userId = localStorage.getItem('userId');
    const lobbyCode = this.extractLobbyCodeFromUrl();

    if (!userId || !lobbyCode) {
      console.error('Brak wymaganych danych: userId lub lobbyCode.');
      return;
    }

    this.lobbyService
      .savePreferences(lobbyCode, +userId, this.selectedPlatform, this.selectedGenre, this.selectedType)
      .subscribe({
        next: () => {
          console.log('Preferencje zapisane pomyślnie.');
        },
        error: (err) => {
          console.error('Nie udało się zapisać preferencji:', err);
        },
      });
  }


  extractLobbyCodeFromUrl(): string | null {
    const url = window.location.href;
    const parts = url.split('/');
    const code = parts[parts.length - 1];
    console.log('Extracted lobby code:', code);
    return code || null;
  }

}
