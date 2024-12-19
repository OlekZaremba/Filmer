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

      this.drawService.submitVote(this.lobbyCode, filmId, this.userId).subscribe({
        next: () => {
          console.log(`Film ${accepted ? 'zaakceptowany' : 'odrzucony'}`);
          this.currentFilmIndex++;
          if (this.currentFilmIndex >= this.films.length) {
            console.log('Przenoszenie użytkownika na stronę wyników...');
            this.router.navigate(['/results', this.lobbyCode], { queryParams: { lobbyCode: this.lobbyCode } });
          }
        },
        error: (err) => console.error('Błąd podczas głosowania:', err)
      });
    } else {
      console.error('Błąd: Brak wymaganych danych (lobbyCode, userId lub filmId).');
    }
  }
}
