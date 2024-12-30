import { Component, OnInit, Renderer2 } from '@angular/core';
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
  currentTheme: 'light' | 'dark' = 'dark'; // Domyślny motyw

  constructor(
    private authService: AuthService,
    private friendsService: FriendsService,
    private renderer: Renderer2
  ) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  ngOnInit() {
    this.isLoggedIn$.subscribe((isLoggedIn) => {
      if (isLoggedIn) {
        this.loadUserPhoto();
      } else {
        this.userPhoto = null;
      }
    });
    this.loadTheme();
  }

  toggleTheme() {
    this.currentTheme = this.currentTheme === 'dark' ? 'light' : 'dark';
    this.applyTheme(this.currentTheme);
    localStorage.setItem('theme', this.currentTheme);
  }

  private applyTheme(theme: 'light' | 'dark') {
    if (theme === 'dark') {
      this.renderer.addClass(document.body, 'dark-theme');
      this.renderer.removeClass(document.body, 'light-theme');
    } else {
      this.renderer.addClass(document.body, 'light-theme');
      this.renderer.removeClass(document.body, 'dark-theme');
    }
  }

  private loadTheme() {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

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

  login() {
    this.authService.login('email@example.com', 'password', 'captcha-mock-response');
  }

  logout() {
    this.authService.logout();
  }
}
