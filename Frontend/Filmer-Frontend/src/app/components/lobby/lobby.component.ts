import {Component, OnInit, OnDestroy, Renderer2} from '@angular/core';
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
  readyParticipantsCount: number = 0;
  currentTheme: 'light' | 'dark' = 'dark';
  currentLanguage: 'polish' | 'english' = 'polish';

  private participantSubscription: Subscription | undefined;

  constructor(
    private friendsService: FriendsService,
    private lobbyService: LobbyService,
    private router: Router,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {
    this.loadTheme();
    this.loadLanguage();
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
      this.loadReadyStatus();
      this.checkGameStart();
    });

    this.loadParticipants();
    window.addEventListener('storage', this.handleStorageChange.bind(this));
  }

  private loadLanguage(): void {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'polish';
  }

  private loadTheme() {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  private applyTheme(theme: 'light' | 'dark') {
    const container = document.querySelector('.bg') as HTMLElement;
    if (theme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }

  ngOnDestroy(): void {
    if (this.participantSubscription) {
      this.participantSubscription.unsubscribe();
    }
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
  }

  loadReadyStatus(): void {
    const lobbyCode = this.extractLobbyCodeFromUrl();
    if (lobbyCode) {
      this.lobbyService.getParticipants(lobbyCode).subscribe({
        next: (participants) => {
          this.readyParticipantsCount = participants.filter(p => p.isReady).length;
        },
        error: (err) => {
          console.error('Nie udało się pobrać statusu gotowości:', err);
        },
      });
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
      alert('Nie można zapisać preferencji: brakuje danych użytkownika lub kodu lobby.');
      return;
    }

    this.lobbyService
      .savePreferences(lobbyCode, +userId, this.selectedPlatform, this.selectedGenre, this.selectedType)
      .subscribe({
        next: (response) => {
          if (response.status === 200) {
            console.log('Preferencje zapisane pomyślnie:', response.body);
            const message = response.body?.message || 'Preferencje zostały zapisane pomyślnie.';
            alert(message);
          } else {
            console.error('Nieoczekiwany status odpowiedzi:', response.status);
            alert('Wystąpił problem z zapisywaniem preferencji. Spróbuj ponownie.');
          }
        },
        error: (err) => {
          if (err.status === 400) {
            console.error('Nie udało się zapisać preferencji:', err.error);
            const errorMessage = err.error?.error || 'Wystąpił błąd podczas zapisywania preferencji. Sprawdź dane i spróbuj ponownie.';
            alert(errorMessage);
          } else {
            console.error('Nieznany błąd:', err);
            alert('Nieznany błąd. Skontaktuj się z administratorem.');
          }
        },
      });
  }




  startGame(): void {
    const lobbyCode = this.extractLobbyCodeFromUrl();
    if (!lobbyCode) {
      console.error('Nieprawidłowy kod lobby.');
      return;
    }

    this.lobbyService.getReadyStatus(lobbyCode).subscribe({
      next: (allReady) => {
        if (allReady) {
          alert('Gra się rozpoczyna!');
          this.lobbyService.startGame(lobbyCode).subscribe({
            next: () => {
              console.log('Gra została rozpoczęta.');
              this.router.navigate([`/draw/${lobbyCode}`]);
            },
            error: (err) => {
              console.error('Wystąpił problem podczas rozpoczynania gry:', err);
            },
          });
        } else {
          alert('Nie wszyscy uczestnicy są gotowi!');

        }
      },
      error: (err) => {
        console.error('Nie udało się sprawdzić statusu gotowości uczestników:', err);
      },
    });
  }



  checkGameStart(): void {
    const lobbyCode = this.extractLobbyCodeFromUrl();
    if (!lobbyCode) {
      console.error('Nieprawidłowy kod lobby.');
      return;
    }

    this.lobbyService.isGameStarted(lobbyCode).subscribe({
      next: (isStarted) => {
        if (isStarted) {
          console.log('Gra została rozpoczęta. Przenoszę na stronę gry.');
          this.router.navigate([`/draw/${lobbyCode}`]);
        }
      },
      error: (err) => {
        console.error('Błąd podczas sprawdzania statusu gry:', err);
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
