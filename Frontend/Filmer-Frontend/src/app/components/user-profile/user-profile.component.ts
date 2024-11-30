import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import {NgFor, NgIf} from '@angular/common';

@Component({
  selector: 'app-user-profile',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css'],
})
export class UserProfileComponent implements OnInit {
  userName: string = '';
  email: string = '';
  friends: any[] = [];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    const email = localStorage.getItem('email');
    if (email) {
      this.userService.getUserDetails(email).subscribe({
        next: (user) => {
          this.userName = user.nick; // Nazwa użytkownika
          this.email = user.email;

          // Pobierz znajomych użytkownika
          this.userService.getFriends(user.id_user).subscribe({
            next: (friends) => {
              this.friends = friends;
            },
            error: (err) => {
              console.error('Nie udało się pobrać listy znajomych:', err);
            },
          });
        },
        error: (err) => {
          console.error('Nie udało się pobrać szczegółów użytkownika:', err);
        },
      });
    } else {
      console.error('E-mail użytkownika nie jest dostępny w localStorage.');
    }
  }
}
