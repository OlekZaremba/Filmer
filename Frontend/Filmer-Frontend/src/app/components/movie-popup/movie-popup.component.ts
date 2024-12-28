import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgIf } from '@angular/common';
import {PdfService} from '../../services/pdf.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-movie-popup',
  standalone: true,
  imports: [NgIf, FormsModule],
  template: `
    <div class="popup-overlay" *ngIf="isVisible" (click)="closePopup()">
      <div class="popup-content" (click)="$event.stopPropagation()">
        <h2>{{ title }}</h2>
        <p>{{ description }}</p>
        <h2>Recenzja</h2>
        <input list="review" placeholder="Twoja recenzja" onfocus="this.value=''">
        <button class="close-button">Zapisz</button>
        <button class="download-button" (click)="downloadPdf()">Pobierz opis do PDF</button>
        <button class="close-button" (click)="closePopup()">Zamknij</button>
      </div>
    </div>

    <datalist id="review">
        <option value="Brak opinii">
        <option value="1">
        <option value="2">
        <option value="3">
        <option value="4">
        <option value="5">
        <option value="6">
        <option value="7">
        <option value="8">
        <option value="9">
        <option value="10">
    </datalist>
  `,
  styleUrls: ['./movie-popup.component.css'],
})
export class MoviePopupComponent {
  @Input() isVisible = false;
  @Input() title = 'Tytuł';
  @Input() description = 'Opis';
  @Output() popupClosed = new EventEmitter<void>();

  constructor(private pdfService: PdfService) {}

  closePopup(): void {
    this.isVisible = false
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

}
