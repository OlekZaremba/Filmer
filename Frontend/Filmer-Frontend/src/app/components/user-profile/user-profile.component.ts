import {ChangeDetectorRef, Component, OnInit, Renderer2} from '@angular/core';
import { NgFor, NgIf } from '@angular/common';
import { FriendsService } from '../../services/friends.service';
import {MenuComponent} from '../menu/menu.component';
import { FormsModule } from '@angular/forms';
import {RouterModule} from '@angular/router';

/**
 * Komponent odpowiedzialny za wyświetlanie i zarządzanie profilem użytkownika.
 * Obsługuje m.in. listę znajomych, ustawienia motywu i języka oraz zarządzanie zdjęciem profilowym.
 * @export
 * @class UserProfileComponent
 * @implements {OnInit}
 */
@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [NgIf, NgFor, FormsModule, RouterModule, MenuComponent],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  /**
   * Nazwa użytkownika.
   * @type {string}
   */
  userName: string = '';

  /**
   * Adres e-mail użytkownika.
   * @type {string}
   */
  email: string = '';

  /**
   * Lista znajomych użytkownika.
   * @type {any[]}
   */
  friends: any[] = [];

  /**
   * Wyszukiwane hasło.
   * @type {string}
   */
  searchTerm: string = '';

  /**
   * Wyniki wyszukiwania użytkowników.
   * @type {any[]}
   */
  searchResults: any[] = [];

  /**
   * Wybrany plik zdjęcia profilowego.
   * @type {File | null}
   */
  selectedFile: File | null = null;

  /**
   * Obecny motyw aplikacji ('light' lub 'dark').
   * @type {'light' | 'dark'}
   */
  currentTheme: 'light' | 'dark' = 'dark';

  /**
   * Obecny język aplikacji ('polish' lub 'english').
   * @type {'polish' | 'english'}
   */
  currentLanguage: 'polish' | 'english' = 'english';

  /**
   * Tworzy instancję UserProfileComponent.
   * @param {FriendsService} friendsService Serwis obsługujący znajomych.
   * @param {Renderer2} renderer Renderer Angulara.
   * @param {ChangeDetectorRef} cdr Referencja do detektora zmian.
   */
  constructor(private friendsService: FriendsService, private renderer: Renderer2, private cdr: ChangeDetectorRef) {
  }

  /**
   * Metoda inicjalizująca komponent.
   * Ładuje dane użytkownika, motyw i język oraz ustawia nasłuch na zmiany w localStorage.
   */
  ngOnInit(): void {
    this.loadTheme();
    this.loadLanguage();

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
    window.addEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Metoda wywoływana przy zniszczeniu komponentu.
   * Usuwa nasłuch na zmiany w localStorage.
   */
  ngOnDestroy(): void {
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Ładuje preferowany język użytkownika z localStorage.
   * @private
   */
  private loadLanguage() {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'english';
    this.cdr.detectChanges();
  }

  /**
   * Obsługuje zmiany w localStorage, np. zmianę języka.
   * @private
   * @param {StorageEvent} event Zdarzenie zmiany w localStorage.
   */
  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
  }

  /**
   * Ładuje zapisany motyw użytkownika z localStorage i stosuje go w interfejsie.
   * Jeśli brak danych w localStorage, domyślnie stosowany jest motyw 'dark'.
   * @private
   */
  private loadTheme(): void {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  /**
   * Stosuje wybrany motyw ('light' lub 'dark') do interfejsu użytkownika.
   * @private
   * @param {'light' | 'dark'} theme Wybrany motyw.
   */
  private applyTheme(theme: 'light' | 'dark'): void {
    const container = document.querySelector('.section-user') as HTMLElement;
    if (theme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }

  /**
   * Pobiera listę znajomych użytkownika z serwera.
   * @param {number} userId ID użytkownika.
   */
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

  /**
   * Pobiera zdjęcie profilowe danego znajomego i przypisuje je do obiektu znajomego.
   * Jeśli zdjęcie nie jest dostępne, stosuje domyślną ikonę użytkownika.
   * @param {any} friend Obiekt znajomego.
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
   * Pobiera zdjęcie profilowe użytkownika i ustawia je w interfejsie.
   * @param {number} userId ID użytkownika.
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
   * Wyszukuje użytkowników na podstawie wpisanego hasła i aktualizuje wyniki wyszukiwania.
   * Dodaje domyślne zdjęcia profilowe dla użytkowników bez zdjęcia.
   */
  searchUsers(): void {
    if (this.searchTerm.trim()) {
      this.friendsService.searchUsers(this.searchTerm).subscribe({
        next: (results) => {
          this.searchResults = results;

          this.searchResults.forEach(user => {
            this.friendsService.getProfilePicture(user.id).subscribe({
              next: (blob) => {
                const objectURL = URL.createObjectURL(blob);
                user.avatar = objectURL;
              },
              error: () => {
                user.avatar = 'assets/images/user.png';
              }
            });
          });
        },
        error: (err) => {
          console.error('Błąd podczas wyszukiwania użytkowników:', err);
        }
      });
    } else {
      this.searchResults = [];
    }
  }

  /**
   * Dodaje użytkownika do listy znajomych.
   * @param {number} friendId ID znajomego do dodania.
   */
  addFriend(friendId: number): void {
    const userId = localStorage.getItem('userId');

    if (userId) {
      this.friendsService.addFriend(+userId, friendId).subscribe({
        next: () => {
          alert('Użytkownik został dodany do znajomych.');
          this.loadFriends(+userId);
          this.searchResults = this.searchResults.filter(user => user.id !== friendId);
        },
        error: (err) => {
          console.error('Błąd podczas dodawania znajomego:', err);
        },
      });
    } else {
      console.error('Brak userId w localStorage.');
    }
  }

  /**
   * Otwiera okno wyboru pliku do przesłania zdjęcia profilowego.
   */
  triggerFileInput(): void {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  /**
   * Obsługuje wybór pliku zdjęcia profilowego przez użytkownika.
   * @param {Event} event Zdarzenie wyboru pliku.
   */
  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files[0]) {
      this.selectedFile = input.files[0];
      this.uploadProfilePicture();
    }
  }

  /**
   * Przesyła wybrane zdjęcie profilowe użytkownika na serwer.
   * W razie powodzenia ładuje nowe zdjęcie do interfejsu.
   */
  uploadProfilePicture(): void {
    if (!this.selectedFile) {
      alert('Nie wybrano zdjęcia.');
      return;
    }

    const userId = localStorage.getItem('userId');
    if (!userId) {
      console.error('Nie znaleziono ID użytkownika w localStorage.');
      return;
    }

    const formData = new FormData();
    formData.append('file', this.selectedFile);

    this.friendsService.uploadProfilePicture(+userId, formData).subscribe({
      next: () => {
        alert('Zdjęcie zostało zapisane.');
        this.loadProfilePicture(+userId);
      },
      error: (err) => {
        console.error('Błąd podczas przesyłania zdjęcia:', err);
        window.location.reload();
      },
    });
  }
}
