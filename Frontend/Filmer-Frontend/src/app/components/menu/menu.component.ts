import { Component, OnInit, Renderer2 } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FriendsService } from '../../services/friends.service';
import { AsyncPipe, NgIf } from '@angular/common';

/**
 * Komponent odpowiedzialny za menu nawigacyjne aplikacji.
 * Zawiera funkcje logowania, wylogowania, zmiany języka, motywu oraz wyświetlania zdjęcia użytkownika.
 * @export
 * @class MenuComponent
 * @implements {OnInit}
 */
@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [RouterModule, AsyncPipe, NgIf],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  /**
   * Strumień określający, czy użytkownik jest zalogowany.
   * @type {Observable<boolean>}
   */
  isLoggedIn$;

  /**
   * Adres URL zdjęcia użytkownika.
   * @type {string | null}
   */
  userPhoto: string | null = null;

  /**
   * Aktualny motyw aplikacji (jasny lub ciemny).
   * @type {'light' | 'dark'}
   */
  currentTheme: 'light' | 'dark' = 'dark';

  /**
   * Aktualny język aplikacji.
   * @type {'polish' | 'english'}
   */
  currentLanguage: 'polish' | 'english' = 'english';

  /**
   * Tworzy instancję komponentu.
   * @param {AuthService} authService - Usługa odpowiedzialna za uwierzytelnianie użytkownika.
   * @param {FriendsService} friendsService - Usługa do zarządzania znajomymi i pobierania zdjęcia użytkownika.
   * @param {Renderer2} renderer - Renderer do manipulacji DOM.
   */
  constructor(
    private authService: AuthService,
    private friendsService: FriendsService,
    private renderer: Renderer2
  ) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  /**
   * Inicjalizuje komponent, ładuje zdjęcie użytkownika, język oraz motyw aplikacji.
   */
  ngOnInit(): void {
    this.isLoggedIn$.subscribe((isLoggedIn) => {
      if (isLoggedIn) {
        this.loadUserPhoto();
      } else {
        this.userPhoto = null;
      }
    });
    this.loadTheme();
    this.loadLanguage();
  }

  /**
   * Zmienia język aplikacji na przeciwny (polski/angielski).
   * Zapisuje zmiany w localStorage i odświeża stronę.
   */
  changeLanguage(): void {
    this.currentLanguage = this.currentLanguage === 'english' ? 'polish' : 'english';
    localStorage.setItem('language', this.currentLanguage);

    window.location.reload();
  }

  /**
   * Ładuje zapisany język aplikacji z localStorage.
   * Jeśli język nie jest zapisany, domyślnie ustawia język angielski.
   * @private
   */
  private loadLanguage(): void {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'english';
  }

  /**
   * Przełącza motyw aplikacji między jasnym a ciemnym.
   * Zapisuje zmiany w localStorage.
   */
  toggleTheme(): void {
    this.currentTheme = this.currentTheme === 'dark' ? 'light' : 'dark';
    this.applyTheme(this.currentTheme);
    localStorage.setItem('theme', this.currentTheme);
  }

  /**
   * Zastosowuje wybrany motyw aplikacji do elementu DOM.
   * @private
   * @param {'light' | 'dark'} theme - Wybrany motyw aplikacji.
   */
  private applyTheme(theme: 'light' | 'dark'): void {
    if (theme === 'dark') {
      this.renderer.addClass(document.body, 'dark-theme');
      this.renderer.removeClass(document.body, 'light-theme');
    } else {
      this.renderer.addClass(document.body, 'light-theme');
      this.renderer.removeClass(document.body, 'dark-theme');
    }
  }

  /**
   * Ładuje zapisany motyw aplikacji z localStorage.
   * Jeśli motyw nie jest zapisany, domyślnie ustawia motyw ciemny.
   * @private
   */
  private loadTheme(): void {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  /**
   * Ładuje zdjęcie profilowe użytkownika.
   * Jeśli użytkownik nie ma zdjęcia, ustawiane jest zdjęcie domyślne.
   * @private
   */
  private loadUserPhoto(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.friendsService.getProfilePicture(+userId).subscribe(
        (blob) => {
          const objectURL = URL.createObjectURL(blob);
          this.userPhoto = objectURL;
        },
        () => {
          this.userPhoto = 'assets/images/user.png';
        }
      );
    } else {
      this.userPhoto = 'assets/images/user.png';
    }
  }

  /**
   * Wywołuje proces logowania za pomocą usługi AuthService.
   */
  login(): void {
    this.authService.login('email@example.com', 'password', 'captcha-mock-response');
  }

  /**
   * Wywołuje proces wylogowania za pomocą usługi AuthService.
   */
  logout(): void {
    this.authService.logout();
  }
}
