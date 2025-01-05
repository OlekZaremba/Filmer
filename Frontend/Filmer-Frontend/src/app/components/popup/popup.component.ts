import { Component, EventEmitter, Input, Output } from '@angular/core';
import { NgIf, NgForOf } from '@angular/common';
/**
 * Komponent PopUpComponent odpowiedzialny za wyświetlanie popupu z listą elementów.
 * Umożliwia zamknięcie popupu oraz przekazywanie zdarzenia informującego o zamknięciu.
 * @export
 * @class PopUpComponent
 */
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
  /**
   * Flaga określająca, czy popup jest widoczny.
   * @type {boolean}
   */
  @Input() isVisible = false;

  /**
   * Tytuł popupu, wyświetlany w nagłówku.
   * @type {string}
   */
  @Input() title = 'Lista filmów';

  /**
   * Lista elementów do wyświetlenia w popupie.
   * @type {string[]}
   */
  @Input() items: string[] = [];

  /**
   * Zdarzenie emitowane po zamknięciu popupu.
   * @type {EventEmitter<void>}
   */
  @Output() popupClosed = new EventEmitter<void>();

  /**
   * Zamknięcie popupu i wyemitowanie zdarzenia `popupClosed`.
   */
  closePopup(): void {
    this.popupClosed.emit();
  }
}
