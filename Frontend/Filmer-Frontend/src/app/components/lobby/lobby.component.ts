import { Component, OnInit, OnDestroy, Renderer2 } from '@angular/core';
import { FriendsService } from '../../services/friends.service';
import { LobbyService } from '../../services/lobby.service';
import { NgForOf, NgIf } from '@angular/common';
import { Router } from '@angular/router';
import { interval, Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';

/**
 * Komponent zarządzający lobby, w którym użytkownicy mogą dołączać, wysyłać zaproszenia,
 * ustawiać preferencje oraz monitorować status uczestników.
 * @export
 * @class LobbyComponent
 * @implements {OnInit}
 * @implements {OnDestroy}
 */
@Component({
  selector: 'app-lobby',
  standalone: true,
  templateUrl: './lobby.component.html',
  imports: [NgForOf, NgIf, FormsModule],
  styleUrls: ['./lobby.component.css'],
})
export class LobbyComponent implements OnInit, OnDestroy {
  /**
   * Nazwa użytkownika.
   * @type {string}
   */
  userName: string = '';

  /**
   * Email użytkownika.
   * @type {string}
   */
  email: string = '';

  /**
   * Lista znajomych użytkownika.
   * @type {any[]}
   */
  friends: any[] = [];

  /**
   * Link do lobby.
   * @type {string}
   */
  lobbyLink: string = '';

  /**
   * Lista uczestników lobby.
   * @type {any[]}
   */
  participants: any[] = [];

  /**
   * Flaga wskazująca, czy można wysyłać zaproszenia.
   * @type {boolean}
   */
  canSendInvites: boolean = false;

  /**
   * Flaga wskazująca, czy lobby jest obecnie generowane.
   * @type {boolean}
   */
  isGeneratingLobby: boolean = false;

  /**
   * Wybrana platforma streamingowa.
   * @type {string}
   */
  selectedPlatform: string = '';

  /**
   * Wybrany gatunek filmowy.
   * @type {string}
   */
  selectedGenre: string = '';

  /**
   * Wybrany typ (np. film lub serial).
   * @type {string}
   */
  selectedType: string = '';

  /**
   * Liczba uczestników gotowych do gry.
   * @type {number}
   */
  readyParticipantsCount: number = 0;

  /**
   * Aktualny motyw aplikacji (jasny lub ciemny).
   * @type {'light' | 'dark'}
   */
  currentTheme: 'light' | 'dark' = 'dark';

  /**
   * Aktualny język aplikacji.
   * @type {'polish' | 'english'}
   */
  currentLanguage: 'polish' | 'english' = 'polish';

  /**
   * Subskrypcja interwału odświeżania listy uczestników.
   * @type {Subscription | undefined}
   */
  private participantSubscription: Subscription | undefined;

  /**
   * Tworzy instancję komponentu.
   * @param {FriendsService} friendsService - Usługa zarządzająca znajomymi użytkownika.
   * @param {LobbyService} lobbyService - Usługa zarządzająca danymi lobby.
   * @param {Router} router - Router Angular do nawigacji.
   * @param {Renderer2} renderer - Renderer do manipulacji DOM.
   */
  constructor(
    private friendsService: FriendsService,
    private lobbyService: LobbyService,
    private router: Router,
    private renderer: Renderer2
  ) {
  }

  /**
   * Inicjalizuje komponent, ładuje dane użytkownika, znajomych, uczestników lobby,
   * język oraz motyw aplikacji. Ustawia nasłuchiwanie na zmiany w localStorage.
   */
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

  /**
   * Usuwa subskrypcję interwału oraz nasłuchiwanie na zmiany w localStorage.
   */
  ngOnDestroy(): void {
    if (this.participantSubscription) {
      this.participantSubscription.unsubscribe();
    }
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Ładuje język aplikacji z localStorage.
   * @private
   */
  private loadLanguage(): void {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'polish';
  }

  /**
   * Ładuje zapisany motyw aplikacji z localStorage.
   * @private
   */
  private loadTheme(): void {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  /**
   * Zastosowuje wybrany motyw aplikacji do elementu DOM.
   * @private
   * @param {'light' | 'dark'} theme - Wybrany motyw aplikacji.
   */
  private applyTheme(theme: 'light' | 'dark'): void {
    const container = document.querySelector('.bg') as HTMLElement;
    if (theme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }

  /**
   * Obsługuje zmiany w localStorage, np. zmiany języka.
   * @private
   * @param {StorageEvent} event - Wydarzenie zmiany w localStorage.
   */
  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
  }

  /**
   * Ładuje status gotowości uczestników lobby.
   */
  loadReadyStatus(): void {
    const lobbyCode = this.extractLobbyCodeFromUrl();
    if (lobbyCode) {
      this.lobbyService.getParticipants(lobbyCode).subscribe({
        next: (participants) => {
          this.readyParticipantsCount = participants.filter((p) => p.isReady).length;
        },
        error: (err) => {
          console.error('Nie udało się pobrać statusu gotowości:', err);
        },
      });
    }
  }

  /**
   * Generuje link do lobby i zapisuje go w localStorage.
   * Po pomyślnym wygenerowaniu lobby użytkownik jest przekierowywany na stronę lobby.
   */
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

  /**
   * Dodaje użytkownika do lobby, jeśli lobbyCode jest obecny w URL.
   * Po dodaniu użytkownika lista uczestników zostaje odświeżona.
   * @param {number} userId - Identyfikator użytkownika.
   */
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

  /**
   * Sprawdza, czy tworzenie nowego lobby jest zablokowane.
   * Zwraca `true`, jeśli lobby jest już generowane lub link do lobby istnieje.
   * @returns {boolean} - Flaga wskazująca, czy tworzenie nowego lobby jest zablokowane.
   */
  isCreatingNewLobbyDisabled(): boolean {
    return this.isGeneratingLobby || !!this.lobbyLink;
  }

  /**
   * Ładuje listę znajomych użytkownika i ich zdjęcia profilowe.
   * @param {number} userId - Identyfikator użytkownika.
   */
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

  /**
   * Ładuje zdjęcie profilowe znajomego.
   * Jeśli wystąpi błąd, ustawia domyślny awatar dla znajomego.
   * @param {any} friend - Obiekt znajomego zawierający jego dane.
   */
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

  /**
   * Ładuje zdjęcie profilowe użytkownika.
   * Jeśli wystąpi błąd, zdjęcie profilowe nie zostanie załadowane.
   * @param {number} userId - Identyfikator użytkownika.
   */
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

  /**
   * Wysyła zaproszenie do znajomego za pomocą emaila.
   * Używa wygenerowanego linku do lobby jako treści zaproszenia.
   * @param {any} friend - Obiekt znajomego, który ma otrzymać zaproszenie.
   */
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

  /**
   * Pobiera listę uczestników lobby.
   * Jeśli kod lobby istnieje, dane uczestników są ładowane z backendu.
   */
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

  /**
   * Zapisuje preferencje użytkownika dotyczące platformy, gatunku i typu w kontekście lobby.
   * Jeśli dane są niekompletne, wyświetlane jest ostrzeżenie.
   */
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

  /**
   * Rozpoczyna grę w lobby, jeśli wszyscy uczestnicy są gotowi.
   * Jeśli nie wszyscy uczestnicy są gotowi, wyświetlane jest ostrzeżenie.
   */
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

  /**
   * Sprawdza, czy gra w lobby została już rozpoczęta.
   * Jeśli gra została rozpoczęta, użytkownik jest przekierowywany na stronę gry.
   */
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

  /**
   * Wyciąga kod lobby z aktualnego URL.
   * @returns {string | null} Kod lobby, jeśli istnieje; w przeciwnym razie `null`.
   */
  extractLobbyCodeFromUrl(): string | null {
    const url = window.location.href;
    const parts = url.split('/');
    const code = parts[parts.length - 1];
    console.log('Extracted lobby code:', code);
    return code || null;
  }
}
