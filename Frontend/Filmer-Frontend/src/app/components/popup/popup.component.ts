import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgIf, NgForOf } from '@angular/common'; // Importujemy NgForOf

@Component({
  selector: 'app-popup',
  standalone: true,
  imports: [NgIf, NgForOf],
  template: `
      <div class="popup-overlay" *ngIf="isVisible" (click)="closePopup()">
          <div class="popup-content" (click)="$event.stopPropagation()">
              <h2>{{ title }}</h2>
              <ul>
                  <li *ngFor="let item of items">{{ item }}</li>
              </ul>
              <button (click)="closePopup()">Zamknij</button>
          </div>
      </div>
  `,
  styleUrls: ['./popup.component.css'],
})
export class PopUpComponent {
  @Input() isVisible = false;
  @Input() title = 'Lista filmów';
  @Input() items: string[] = [];
  @Output() popupClosed = new EventEmitter<void>();

  closePopup() {
    this.popupClosed.emit();
  }
}
