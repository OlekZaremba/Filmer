import { ChangeDetectorRef, Component, Renderer2 } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { RecaptchaModule } from 'ng-recaptcha';

/**
 * Komponent zarządzający stroną logowania i rejestracji użytkownika.
 * Zawiera funkcjonalności logowania, rejestracji i obsługi CAPTCHA.
 * @export
 * @class LoginPageComponent
 */
@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [FormsModule, RouterModule, RecaptchaModule],
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {
  /**
   * Email użytkownika używany podczas logowania.
   * @type {string}
   */
  loginEmail: string = '';

  /**
   * Hasło użytkownika używane podczas logowania.
   * @type {string}
   */
  loginPassword: string = '';

  /**
   * Nazwa użytkownika wprowadzana podczas rejestracji.
   * @type {string}
   */
  registerUsername: string = '';

  /**
   * Email użytkownika wprowadzany podczas rejestracji.
   * @type {string}
   */
  registerEmail: string = '';

  /**
   * Hasło użytkownika wprowadzane podczas rejestracji.
   * @type {string}
   */
  registerPassword: string = '';

  /**
   * Potwierdzenie hasła użytkownika wprowadzane podczas rejestracji.
   * @type {string}
   */
  registerConfirmPassword: string = '';

  /**
   * Klucz strony dla reCAPTCHA.
   * @type {string}
   */
  captchaSiteKey: string = '6LfkyJYqAAAAAJ_woX_R6Ida3127aiF68n6VCQuL';

  /**
   * Odpowiedź reCAPTCHA użytkownika.
   * @type {string | null}
   */
  captchaResponse: string | null = null;

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
   * @param {Router} router - Router Angular do nawigacji między widokami.
   * @param {Renderer2} renderer - Renderer do manipulacji DOM.
   * @param {ChangeDetectorRef} cdr - Usługa wymuszająca odświeżenie widoku.
   */
  constructor(
    private authService: AuthService,
    private router: Router,
    private renderer: Renderer2,
    private cdr: ChangeDetectorRef
  ) {}

  /**
   * Inicjalizuje komponent, ładuje motyw i język aplikacji.
   * Dodaje nasłuchiwanie na zmiany w localStorage.
   */
  ngOnInit(): void {
    this.loadTheme();
    this.loadLanguage();

    window.addEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Usuwa nasłuchiwanie na zmiany w localStorage po zniszczeniu komponentu.
   */
  ngOnDestroy(): void {
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Ładuje język aplikacji z localStorage i odświeża widok.
   * @private
   */
  private loadLanguage(): void {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'english';
    this.cdr.detectChanges();
  }

  /**
   * Obsługuje zmiany w localStorage, np. zmianę języka.
   * @private
   * @param {StorageEvent} event - Wydarzenie zmiany w localStorage.
   */
  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
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
    const container = document.querySelector('.container') as HTMLElement;
    if (theme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }

  /**
   * Obsługuje logowanie użytkownika.
   * Wymaga rozwiązanej reCAPTCHA.
   */
  login(): void {
    if (!this.captchaResponse) {
      console.error('Captcha nie została rozwiązana.');
      return;
    }

    this.authService.login(this.loginEmail, this.loginPassword, this.captchaResponse).subscribe({
      next: (response) => {
        console.log(response.message);
        this.router.navigate(['/']);
      },
      error: (error) => {
        if (error.status === 401) {
          console.error('Unauthorized: Invalid email or password');
        } else {
          console.error('Login failed', error);
        }
      }
    });
  }

  /**
   * Obsługuje rejestrację nowego użytkownika.
   * W przypadku sukcesu wyświetla komunikat i resetuje formularz.
   */
  register(): void {
    this.authService.register(
      this.registerUsername,
      this.registerEmail,
      this.registerPassword,
      this.registerConfirmPassword
    ).subscribe({
      next: (response) => {
        console.log(response.message);
      },
      error: (error) => {
        if (error.status === 409) {
          console.error('Conflict: User already exists');
        } else if (error.status === 201) {
          console.log('User registered successfully');
          this.registerUsername = '';
          this.registerEmail = '';
          this.registerPassword = '';
          this.registerConfirmPassword = '';
          alert('Zarejestrowano poprawnie!');
        } else {
          console.error('Registration failed', error);
        }
      }
    });
  }

  /**
   * Obsługuje rozwiązanie reCAPTCHA przez użytkownika.
   * @param {string | null} captchaResponse - Odpowiedź reCAPTCHA.
   */
  onCaptchaResolved(captchaResponse: string | null): void {
    console.log('Captcha rozwiązana:', captchaResponse);
    this.captchaResponse = captchaResponse;
  }
}
