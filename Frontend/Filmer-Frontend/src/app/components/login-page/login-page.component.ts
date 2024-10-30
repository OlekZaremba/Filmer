import { Component } from '@angular/core';
import {AuthService} from '../../auth.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-login-page',
  standalone: true,
  imports: [ FormsModule],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.css'
})
export class LoginPageComponent {

  email: string = '';
  password: string = '';
  username: string = '';
  confirmPassword: string = '';

  constructor(private authService: AuthService) {}

  login() {
    this.authService.login(this.email, this.password).subscribe({
      next: (response) => {
        console.log(response.message);
      },
      error: (error) => {
        if (error.status === 401) {
          console.error("Unauthorized: Invalid email or password");
        } else {
          console.error("Login failed", error);
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
          console.error("Conflict: User already exists");
        } else if (error.status === 200) {
          console.log("User registered successfully");
        } else {
          console.error("Registration failed", error);
        }
      }
    });
  }
}
