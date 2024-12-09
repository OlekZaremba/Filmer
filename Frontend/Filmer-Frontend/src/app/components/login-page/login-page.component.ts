import { Component } from '@angular/core';
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
  email: string = '';
  password: string = '';
  username: string = '';
  confirmPassword: string = '';
  captchaSiteKey: string = '6LfkyJYqAAAAAJ_woX_R6Ida3127aiF68n6VCQuL'; // Zamień na swój klucz witryny
  captchaResponse: string | null = null;

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    if (!this.captchaResponse) {
      console.error('Captcha nie została rozwiązana.');
      return;
    }

    this.authService.login(this.email, this.password, this.captchaResponse).subscribe({
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
    this.authService.register(this.username, this.email, this.password, this.confirmPassword).subscribe({
      next: (response) => {
        console.log(response.message);
      },
      error: (error) => {
        if (error.status === 409) {
          console.error('Conflict: User already exists');
        } else if (error.status === 200) {
          console.log('User registered successfully');
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
