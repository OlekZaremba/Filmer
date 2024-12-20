import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DrawService, Film } from '../../services/draw.service';
import { ReactiveFormsModule } from '@angular/forms';
import { NgForOf, NgIf } from '@angular/common';

@Component({
  selector: 'app-draw',
  standalone: true,
  imports: [NgForOf, NgIf, ReactiveFormsModule],
  templateUrl: './draw.component.html',
  styleUrl: './draw.component.css'
})
export class DrawComponent implements OnInit {
  lobbyCode: string | null = null;
  userId: number | null = null;
  films: Film[] = [];
  currentFilmIndex = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private drawService: DrawService
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

  fetchDrawFilms(): void {
    this.drawService.getDrawFilms(this.lobbyCode!)
      .subscribe({
        next: (data) => this.films = data,
        error: (err) => console.error('Błąd pobierania filmów:', err)
      });
  }

  acceptFilm(): void {
    this.submitVote(true);
  }

  declineFilm(): void {
    this.submitVote(false);
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
