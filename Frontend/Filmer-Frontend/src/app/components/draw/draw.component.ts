import { Component, OnInit, Renderer2 } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DrawService, Film } from '../../services/draw.service';
import { ReactiveFormsModule } from '@angular/forms';
import { NgClass, NgForOf, NgIf } from '@angular/common';

/**
 * Komponent odpowiedzialny za proces głosowania na filmy w lobby.
 * @export
 * @class DrawComponent
 * @implements {OnInit}
 */
@Component({
  selector: 'app-draw',
  standalone: true,
  imports: [NgForOf, NgIf, ReactiveFormsModule, NgClass],
  templateUrl: './draw.component.html',
  styleUrl: './draw.component.css'
})
export class DrawComponent implements OnInit {
  /**
   * Kod lobby, pobierany z URL.
   * @type {string | null}
   */
  lobbyCode: string | null = null;

  /**
   * Identyfikator użytkownika, pobierany z localStorage.
   * @type {number | null}
   */
  userId: number | null = null;

  /**
   * Lista filmów do głosowania.
   * @type {Film[]}
   */
  films: Film[] = [];

  /**
   * Indeks aktualnie wyświetlanego filmu.
   * @type {number}
   */
  currentFilmIndex = 0;

  /**
   * Flaga oznaczająca animację przesuwania w lewo.
   * @type {boolean}
   */
  isSwipingLeft = false;

  /**
   * Flaga oznaczająca animację przesuwania w prawo.
   * @type {boolean}
   */
  isSwipingRight = false;

  /**
   * Aktualny motyw aplikacji (jasny lub ciemny).
   * @type {'light' | 'dark'}
   */
  currentTheme: 'light' | 'dark' = 'dark';

  /**
   * Tworzy instancję komponentu.
   * @param {ActivatedRoute} route - Usługa do zarządzania trasami i pobierania parametrów URL.
   * @param {Router} router - Router Angular do nawigacji między widokami.
   * @param {DrawService} drawService - Usługa do zarządzania głosowaniem na filmy.
   * @param {Renderer2} renderer - Renderer do manipulacji DOM.
   */
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private drawService: DrawService,
    private renderer: Renderer2
  ) {}

  /**
   * Inicjalizacja komponentu, pobiera kod lobby i identyfikator użytkownika oraz filmy do głosowania.
   */
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

  /**
   * Ustawia motyw aplikacji po wyrenderowaniu widoku.
   */
  ngAfterViewChecked(): void {
    this.applyTheme();
  }

  /**
   * Pobiera listę filmów do głosowania z serwera.
   */
  fetchDrawFilms(): void {
    this.drawService.getDrawFilms(this.lobbyCode!)
      .subscribe({
        next: (data) => this.films = data,
        error: (err) => console.error('Błąd pobierania filmów:', err)
      });
  }

  /**
   * Obsługuje akceptację filmu przez użytkownika (przesunięcie w prawo).
   */
  acceptFilm(): void {
    this.isSwipingRight = true;
    setTimeout(() => {
      this.isSwipingRight = false;
      this.submitVote(true);
    }, 500);
  }

  /**
   * Obsługuje odrzucenie filmu przez użytkownika (przesunięcie w lewo).
   */
  declineFilm(): void {
    this.isSwipingLeft = true;
    setTimeout(() => {
      this.isSwipingLeft = false;
      this.submitVote(false);
    }, 500);
  }

  /**
   * Wysyła głos użytkownika na wybrany film do serwera.
   * @param {boolean} accepted - Czy film został zaakceptowany.
   */
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

  /**
   * Sprawdza, czy użytkownik zakończył głosowanie na wszystkie filmy.
   * Jeśli tak, informuje backend i przekierowuje na stronę wyników.
   */
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

  /**
   * Ładuje zapisany motyw aplikacji z pamięci lokalnej.
   * @private
   */
  private loadTheme(): void {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme();
  }

  /**
   * Zastosowuje aktualny motyw aplikacji do elementu DOM.
   * @private
   */
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
}
