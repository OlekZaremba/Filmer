import {Component, OnInit} from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { AsyncPipe, NgIf } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [RouterModule, AsyncPipe, NgIf],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.css']
})
export class MenuComponent implements OnInit{
  isLoggedIn$;

  constructor(private authService: AuthService) {
    this.isLoggedIn$ = this.authService.isLoggedIn$;
  }

  login() {
    this.authService.login('email@example.com', 'password');
  }

  logout() {
    this.authService.logout();
  }

  ngOnInit() {
    this.isLoggedIn$.subscribe((isLoggedIn) => {
      console.log('Stan isLoggedIn w menu po aktualizacji:', isLoggedIn);
    });
  }
}
