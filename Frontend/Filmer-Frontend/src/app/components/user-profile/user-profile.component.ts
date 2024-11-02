import {Component, OnInit} from '@angular/core';
import {UserService} from '../../services/user.service';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [],
  templateUrl: './user-profile.component.html',
  styleUrl: './user-profile.component.css'
})
export class UserProfileComponent implements OnInit {
  userName: string = '';
  email: string = '';

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    const email = localStorage.getItem('email');
    // const email = '111';
    if (email) {
      this.userService.getUserDetails(email).subscribe({
        next: (user) => {
          this.userName = user.nick;
          this.email = user.email;
        },
        error: (err) => {
          console.error('Nie udało się pobrać danych użytkownika:', err);
        }
      });
    } else {
      console.error('E-mail użytkownika nie jest dostępny w localStorage.');
    }
  }
}
