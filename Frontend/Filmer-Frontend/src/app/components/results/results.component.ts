import {Component, OnInit, Renderer2} from '@angular/core';
import { ActivatedRoute, Router, RouterLink, RouterLinkActive } from '@angular/router';
import { DrawService } from '../../services/draw.service';
import { interval, Subscription } from 'rxjs';
import {CommonModule, NgIf} from '@angular/common';
import { RouterModule } from '@angular/router';
import { PopUpComponent } from '../popup/popup.component';
import {ResultsService} from '../../services/results.service';

/**
 * Komponent ResultsComponent odpowiedzialny za wyświetlanie wyników głosowania
 * w lobby, zarządzanie motywem aplikacji oraz interakcję z pop-upem z informacjami
 * o filmach. Obsługuje mechanizm okresowego sprawdzania statusu głosowania.
 * @export
 * @class ResultsComponent
 * @implements {OnInit}
 */
@Component({
  selector: 'app-results',
  standalone: true,
  imports: [NgIf, RouterLink, RouterLinkActive, RouterModule, PopUpComponent, CommonModule],
  templateUrl: './results.component.html',
  styleUrl: './results.component.css',
})
export class ResultsComponent implements OnInit {
  /**
   * Kod lobby aktualnie otwartego.
   * @type {string | null}
   */
  lobbyCode: string | null = null;

  /**
   * ID użytkownika zalogowanego.
   * @type {number | null}
   */
  userId: number | null = null;

  /**
   * Flaga określająca, czy głosowanie zostało zakończone.
   * @type {boolean}
   */
  votingCompleted = false;

  /**
   * Subskrypcja używana do okresowego sprawdzania statusu głosowania.
   * @type {Subscription | null}
   */
  pollingSubscription: Subscription | null = null;

  /**
   * Wyniki głosowania w lobby.
   * @type {{ [key: number]: string[] }}
   */
  results: { [key: number]: string[] } = {};

  /**
   * Flaga określająca, czy pop-up jest widoczny.
   * @type {boolean}
   */
  isPopupVisible = false;

  /**
   * Tytuł wyświetlany w pop-upie.
   * @type {string}
   */
  popupTitle = '';

  /**
   * Lista elementów wyświetlana w pop-upie.
   * @type {string[]}
   */
  popupItems: string[] = [];

  /**
   * Obecny motyw aplikacji ('light' lub 'dark').
   * @type {'light' | 'dark'}
   */
  currentTheme: 'light' | 'dark' = 'dark';

  /**
   * Tworzy instancję ResultsComponent.
   * @param {ActivatedRoute} route Serwis do obsługi aktywnej trasy.
   * @param {DrawService} drawService Serwis do obsługi statusu głosowania.
   * @param {Router} router Router Angulara.
   * @param {ResultsService} resultsService Serwis do pobierania i wysyłania wyników.
   * @param {Renderer2} renderer Renderer Angulara do modyfikacji DOM.
   */
  constructor(
    private route: ActivatedRoute,
    private drawService: DrawService,
    private router: Router,
    private resultsService: ResultsService,
    private renderer: Renderer2
  ) {}

  /**
   * Inicjalizuje komponent, ustawiając kod lobby i userId oraz rozpoczynając
   * okresowe sprawdzanie statusu głosowania.
   */
  ngOnInit(): void {
    this.lobbyCode = this.route.snapshot.paramMap.get('lobbyCode');
    const storedUserId = localStorage.getItem('userId');

    if (storedUserId) {
      this.userId = Number(storedUserId);
    } else {
      console.error('Brak userId w localStorage.');
      return;
    }

    if (this.lobbyCode) {
      this.startPolling();
    } else {
      console.error('Brak lobbyCode w URL.');
    }
  }

  /**
   * Sprawdza i stosuje motyw po każdej aktualizacji widoku.
   */
  ngAfterViewChecked(): void {
    this.applyTheme(this.currentTheme);
  }

  /**
   * Ładuje motyw z localStorage i stosuje go w interfejsie.
   * @private
   */
  private loadTheme(): void {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  /**
   * Stosuje wybrany motyw ('light' lub 'dark') do interfejsu użytkownika.
   * @private
   * @param {'light' | 'dark'} theme Wybrany motyw.
   */
  private applyTheme(theme: 'light' | 'dark'): void {
    const container = document.querySelector('.section-user') as HTMLElement;
    if (theme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }

  /**
   * Wyświetla pop-up z listą filmów dla wybranego miejsca w głosowaniu.
   * @param {string} place Nazwa miejsca.
   * @param {number} placeKey Klucz miejsca w wynikach.
   */
  showPopupForPlace(place: string, placeKey: number): void {
    this.popupTitle = `Filmy z miejsca ${place}`;
    this.popupItems = (this.results[placeKey] || []).map(
      (film: any) => `${film.filmName} - ${film.filmDesc}`
    );
    this.isPopupVisible = true;
  }

  /**
   * Pobiera wyniki głosowania z serwera.
   */
  loadResults(): void {
    if (this.lobbyCode) {
      this.resultsService.getResults(this.lobbyCode).subscribe({
        next: (results) => {
          console.log('Wyniki pobrane z backendu:', results);
          this.results = results;
        },
        error: (err) => console.error('Błąd podczas pobierania wyników:', err),
      });
    }
  }

  /**
   * Zamyka pop-up.
   */
  closePopup(): void {
    this.isPopupVisible = false;
  }

  /**
   * Rozpoczyna okresowe sprawdzanie statusu głosowania.
   */
  startPolling(): void {
    this.pollingSubscription = interval(5000).subscribe(() => {
      this.checkVotingStatus();
    });
  }

  /**
   * Sprawdza status głosowania w lobby i aktualizuje wyniki, gdy głosowanie
   * zostanie zakończone.
   */
  checkVotingStatus(): void {
    if (this.lobbyCode && this.userId !== null) {
      this.drawService.checkVotingStatus(this.lobbyCode, this.userId).subscribe({
        next: (isCompleted: boolean) => {
          this.votingCompleted = isCompleted;
          if (this.votingCompleted) {
            this.pollingSubscription?.unsubscribe();
            this.loadResults();
          }
        },
        error: (err) => console.error('Błąd podczas sprawdzania statusu głosowania:', err),
      });
    } else {
      console.error('Nie ustawiono userId lub lobbyCode.');
    }
  }

  /**
   * Wysyła wyniki głosowania na e-mail użytkownika.
   */
  sendResults(): void {
    const email = localStorage.getItem('email');
    if (!email || !this.lobbyCode) {
      alert('Brak e-maila użytkownika lub kodu lobby.');
      return;
    }

    this.resultsService.sendResultsEmail(this.lobbyCode, email).subscribe({
      next: () => {
        alert('Wyniki zostały wysłane na Twój e-mail.');
      },
      error: (err) => {
        console.error('Błąd podczas wysyłania wyników:', err);
        alert('Nie udało się wysłać wyników.');
      },
    });
  }
}

