import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import { PdfService } from '../../services/pdf.service';
import { FilmService } from '../../services/film.service';
import { FormsModule } from '@angular/forms';

/**
 * Komponent MoviePopupComponent odpowiedzialny za wyświetlanie popupu z informacjami o filmie,
 * możliwością dodania recenzji oraz pobrania opisu filmu w formacie PDF.
 * @export
 * @class MoviePopupComponent
 * @implements {OnInit}
 */
@Component({
  selector: 'app-movie-popup',
  standalone: true,
  imports: [NgIf, FormsModule, NgFor],
  template: `
    <div class="popup-overlay" *ngIf="isVisible" (click)="closePopup()">
      <div class="popup-content" (click)="$event.stopPropagation()">
        <h2>{{ title }}</h2>
        <p>{{ description }}</p>
        <h2>Recenzja</h2>
        <input
          list="review"
          placeholder="Twoja recenzja"
          [(ngModel)]="currentRating"
          (focus)="clearInputIfDefault()"
        />
        <button class="close-button" (click)="saveRating()">Zapisz</button>
        <button class="download-button" (click)="downloadPdf()">Pobierz opis do PDF</button>
        <button class="close-button" (click)="closePopup()">Zamknij</button>
      </div>
    </div>

    <datalist id="review">
      <option value="Brak opinii"></option>
      <option *ngFor="let r of [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]" [value]="r"></option>
    </datalist>
  `,
  styleUrls: ['./movie-popup.component.css'],
})
export class MoviePopupComponent implements OnInit {
  /**
   * Flaga określająca, czy popup jest widoczny.
   * @type {boolean}
   */
  @Input() isVisible = false;

  /**
   * Tytuł filmu wyświetlany w popupie.
   * @type {string}
   */
  @Input() title!: string;

  /**
   * Opis filmu wyświetlany w popupie.
   * @type {string}
   */
  @Input() description = 'Opis';

  /**
   * Zdarzenie emitowane po zamknięciu popupu.
   * @type {EventEmitter<void>}
   */
  @Output() popupClosed = new EventEmitter<void>();

  /**
   * Aktualna ocena użytkownika dla filmu.
   * @type {string}
   */
  currentRating: string = 'Brak opinii';

  /**
   * Tworzy instancję MoviePopupComponent.
   * @param {PdfService} pdfService Serwis do generowania plików PDF.
   * @param {FilmService} filmService Serwis do obsługi danych o filmach.
   */
  constructor(private pdfService: PdfService, private filmService: FilmService) {}

  /**
   * Inicjalizuje komponent, ładując ocenę filmu użytkownika, jeśli jest dostępna.
   */
  ngOnInit(): void {
    this.loadRating();
  }

  /**
   * Zamyka popup i emituje zdarzenie `popupClosed`.
   */
  closePopup(): void {
    this.isVisible = false;
    this.popupClosed.emit();
  }

  /**
   * Pobiera opis filmu i generuje plik PDF, który można pobrać.
   */
  downloadPdf(): void {
    this.pdfService.generatePdf(this.title, this.description).subscribe((response: Blob) => {
      const url = window.URL.createObjectURL(response);
      const a = document.createElement('a');
      a.href = url;
      a.download = `${this.title}-opis.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  /**
   * Ładuje ocenę filmu użytkownika z serwera, jeśli jest dostępna.
   */
  loadRating(): void {
    const userId = this.getUserId();
    if (userId) {
      this.filmService.getFilmsByName(this.title).subscribe((films) => {
        if (films.length > 0) {
          const film = films[0];
          this.filmService.getRating(film.idFilm, userId).subscribe(
            (rating) => {
              this.currentRating = rating ? rating.toString() : 'Brak opinii';
            },
            (error) => {
              console.log('Nie znaleziono oceny dla filmu:', error);
            }
          );
        } else {
          console.error('Nie znaleziono filmu o nazwie:', this.title);
        }
      });
    } else {
      console.error('ID użytkownika nie jest dostępne w localStorage.');
    }
  }

  /**
   * Zapisuje ocenę filmu użytkownika na serwerze.
   * Waliduje wprowadzone dane przed zapisem.
   */
  saveRating(): void {
    const userId = this.getUserId();
    if (userId && this.currentRating !== 'Brak opinii') {
      const numericRating = parseInt(this.currentRating, 10);
      if (!isNaN(numericRating) && numericRating >= 1 && numericRating <= 10) {
        this.filmService.getFilmsByName(this.title).subscribe((films) => {
          if (films.length > 0) {
            const film = films[0];
            this.filmService.setRating(film.idFilm, userId, numericRating).subscribe(
              (response) => {
                alert(response.message);
              },
              (error) => {
                console.error('Błąd przy zapisie oceny:', error);
              }
            );
          } else {
            console.error('Nie znaleziono filmu o nazwie:', this.title);
          }
        });
      } else {
        alert('Podano nieprawidłową ocenę.');
      }
    } else {
      console.error('Nie można zapisać oceny: brak ID użytkownika lub oceny.');
    }
  }

  /**
   * Czyści pole oceny, jeśli zawiera wartość domyślną.
   */
  clearInputIfDefault(): void {
    if (this.currentRating === 'Brak opinii') {
      this.currentRating = '';
    }
  }

  /**
   * Pobiera ID użytkownika z localStorage.
   * @private
   * @returns {number | null} ID użytkownika lub null, jeśli brak ID w localStorage.
   */
  private getUserId(): number | null {
    const userId = localStorage.getItem('userId');
    return userId ? +userId : null;
  }
}
