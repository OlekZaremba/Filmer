import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgIf } from '@angular/common';
import {PdfService} from '../../services/pdf.service';

@Component({
  selector: 'app-movie-popup',
  standalone: true,
  imports: [NgIf],
  template: `
    <div
      class="popup-overlay"
      *ngIf="isVisible"
      (click)="closePopup()"
    >
      <div
        class="popup-content"
        (click)="$event.stopPropagation()"
      >
        <h2>{{ title }}</h2>
        <p>{{ description }}</p>
        <button class="download-button" (click)="downloadPdf()">Pobierz opis do PDF</button>
        <button class="close-button" (click)="closePopup()">Zamknij</button>
      </div>
    </div>
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
