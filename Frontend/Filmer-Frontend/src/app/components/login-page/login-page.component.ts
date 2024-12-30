import {ChangeDetectorRef, Component, Renderer2} from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { RecaptchaModule } from 'ng-recaptcha';


@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [FormsModule, RouterModule, RecaptchaModule],
  templateUrl: './login-page.component.html',
  styleUrls: ['./login-page.component.css']
})
export class LoginPageComponent {
  // Logowanie
  loginEmail: string = '';
  loginPassword: string = '';

  // Rejestracja
  registerUsername: string = '';
  registerEmail: string = '';
  registerPassword: string = '';
  registerConfirmPassword: string = '';

  captchaSiteKey: string = '6LfkyJYqAAAAAJ_woX_R6Ida3127aiF68n6VCQuL';
  captchaResponse: string | null = null;
  currentTheme: 'light' | 'dark' = 'dark';
  currentLanguage: 'polish' | 'english' = 'english';


  constructor(private authService: AuthService, private router: Router, private renderer: Renderer2, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadTheme();
    this.loadLanguage();

    window.addEventListener('storage', this.handleStorageChange.bind(this));
  }

  ngOnDestroy(): void {
    // Usuwanie nasłuchiwania po zniszczeniu komponentu
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  private loadLanguage() {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'english';
    this.cdr.detectChanges(); // Wymuszenie odświeżenia widoku
  }

  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
  }

  private loadTheme() {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  private applyTheme(theme: 'light' | 'dark') {
    const container = document.querySelector('.container') as HTMLElement;
    if (theme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }

  login() {
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

  register() {
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
          alert('Zarejestrowano poprawnie!')
        } else {
          console.error('Registration failed', error);
        }
      }
    });
  }

  onCaptchaResolved(captchaResponse: string | null): void {
    console.log('Captcha rozwiązana:', captchaResponse);
    this.captchaResponse = captchaResponse;
  }
}
