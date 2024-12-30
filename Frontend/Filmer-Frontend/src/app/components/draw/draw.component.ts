import {Component, OnInit, Renderer2} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DrawService, Film } from '../../services/draw.service';
import { ReactiveFormsModule } from '@angular/forms';
import {NgClass, NgForOf, NgIf} from '@angular/common';

@Component({
  selector: 'app-draw',
  standalone: true,
  imports: [NgForOf, NgIf, ReactiveFormsModule, NgClass],
  templateUrl: './draw.component.html',
  styleUrl: './draw.component.css'
})
export class DrawComponent implements OnInit {
  lobbyCode: string | null = null;
  userId: number | null = null;
  films: Film[] = [];
  currentFilmIndex = 0;
  isSwipingLeft = false;
  isSwipingRight = false;
  currentTheme: 'light' | 'dark' = 'dark';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private drawService: DrawService,
    private renderer: Renderer2
  ) {}

  ngOnInit(): void {

    this.lobbyCode = this.route.snapshot.paramMap.get('lobbyCode');
    const storedUserId = localStorage.getItem('userId');

    if (storedUserId) {
      this.userId = Number(storedUserId);
    } else {
      console.error('Błąd: Brak userId w localStorage. Użytkownik musi być zalogowany.');
      return;
    }

    if (this.lobbyCode) {
      this.fetchDrawFilms();
    } else {
      console.error('Błąd: Brak kodu lobby w URL.');
    }
  }

  ngAfterViewChecked(): void {
    this.applyTheme();
  }

  private loadTheme() {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme();
  }

  private applyTheme(): void {
    const container = document.querySelector('.section-user2') as HTMLElement;
    if (!container) {
      console.warn('Element .section-user2 nie istnieje w DOM.');
      return;
    }

    if (this.currentTheme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }


  fetchDrawFilms(): void {
    this.drawService.getDrawFilms(this.lobbyCode!)
      .subscribe({
        next: (data) => this.films = data,
        error: (err) => console.error('Błąd pobierania filmów:', err)
      });
  }

  acceptFilm(): void {
    this.isSwipingRight = true;
    setTimeout(() => {
      this.isSwipingRight = false;
      this.submitVote(true);
    }, 500); // Czas trwania animacji
  }

  declineFilm(): void {
    this.isSwipingLeft = true;
    setTimeout(() => {
      this.isSwipingLeft = false;
      this.submitVote(false);
    }, 500); // Czas trwania animacji
  }

  submitVote(accepted: boolean): void {
    if (this.lobbyCode && this.userId !== null && this.films.length > this.currentFilmIndex) {
      const filmId = this.films[this.currentFilmIndex].idFilm;

      if (accepted) {
        this.drawService.submitVote(this.lobbyCode, filmId, this.userId).subscribe({
          next: () => {
            console.log(`Film zaakceptowany`);
            this.currentFilmIndex++;
            this.checkIfEndOfVoting();
          },
          error: (err) => console.error('Błąd podczas głosowania:', err)
        });
      } else {
        console.log('Film odrzucony');
        this.currentFilmIndex++;
        this.checkIfEndOfVoting();
      }
    } else {
      console.error('Błąd: Brak wymaganych danych (lobbyCode, userId lub filmId).');
    }
  }

  checkIfEndOfVoting(): void {
    if (this.currentFilmIndex >= this.films.length) {
      if (!this.lobbyCode) {
        console.error('Błąd: Brak kodu lobby.');
        return;
      }

      console.log('Użytkownik zakończył głosowanie. Informowanie backendu...');

      this.drawService.finishVoting(this.lobbyCode, this.userId!).subscribe({
        next: (response) => console.log('Zaktualizowano backend o zakończeniu głosowania:', response),
        error: (err) => console.error('Błąd podczas zgłaszania zakończenia głosowania:', err),
      });

      console.log('Przekierowanie na wyniki dla lobbyCode:', this.lobbyCode);
      this.router.navigate(['/results', this.lobbyCode]);
    }
  }



}
