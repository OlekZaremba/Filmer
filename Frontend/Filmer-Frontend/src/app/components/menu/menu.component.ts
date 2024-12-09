import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FriendsService } from '../../services/friends.service';
import { AsyncPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [RouterModule, AsyncPipe, NgIf],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit {
  isLoggedIn$;
  userPhoto: string | null = null;

  constructor(private authService: AuthService, private friendsService: FriendsService) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  login() {
    this.authService.login('email@example.com', 'password', 'captcha-mock-response');
  }

  logout() {
    this.authService.logout();
  }

  ngOnInit() {
    this.isLoggedIn$.subscribe((isLoggedIn) => {
      console.log('Stan isLoggedIn w menu po aktualizacji:', isLoggedIn);
      if (isLoggedIn) {
        this.loadUserPhoto();
      } else {
        this.userPhoto = null;
      }
    });
  }

  private loadUserPhoto(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.friendsService.getProfilePicture(+userId).subscribe(
        (blob) => {
          const objectURL = URL.createObjectURL(blob);
          this.userPhoto = objectURL;
        },
        (error) => {
          console.error('Błąd podczas pobierania zdjęcia profilowego:', error);
          this.userPhoto = 'assets/images/user.png';
        }
      );
    } else {
      console.error('Nie znaleziono ID użytkownika w localStorage.');
      this.userPhoto = 'assets/images/user.png';
    }
  }
}
