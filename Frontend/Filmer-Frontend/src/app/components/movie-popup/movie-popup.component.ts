import { Component, EventEmitter, Input, Output, OnInit } from '@angular/core';
import {NgFor, NgIf} from '@angular/common';
import { PdfService } from '../../services/pdf.service';
import { FilmService } from '../../services/film.service';
import { FormsModule } from '@angular/forms';

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
  @Input() isVisible = false;
  @Input() title!: string;
  @Input() description = 'Opis';
  @Output() popupClosed = new EventEmitter<void>();
  currentRating: string = 'Brak opinii';

  constructor(private pdfService: PdfService, private filmService: FilmService) {}

  ngOnInit(): void {
    this.loadRating();
  }

  closePopup(): void {
    this.isVisible = false;
    this.popupClosed.emit();
  }

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

  saveRating(): void {
    const userId = this.getUserId();
    if (userId && this.currentRating !== 'Brak opinii') {
      const numericRating = parseInt(this.currentRating, 10);
      if (!isNaN(numericRating) && numericRating >= 1 && numericRating <= 10) {
        this.filmService.getFilmsByName(this.title).subscribe((films) => {
          if (films.length > 0) {
            const film = films[0];
            console.log('Film ID:', film.idFilm, 'User ID:', userId, 'Rating:', numericRating); // Debug
            this.filmService.setRating(film.idFilm, userId, numericRating).subscribe(
              () => {
                alert('Ocena zapisana!');
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


  clearInputIfDefault(): void {
    if (this.currentRating === 'Brak opinii') {
      this.currentRating = '';
    }
  }

  private getUserId(): number | null {
    const userId = localStorage.getItem('userId');
    return userId ? +userId : null;
  }
}
