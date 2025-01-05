import {Component, EventEmitter, Output, Renderer2} from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import {HttpClient} from '@angular/common/http';

/**
 * Komponent odpowiedzialny za wyświetlanie okna popup do dodawania nowych filmów.
 * @export
 * @class AddMoviePopupComponent
 */
@Component({
  selector: 'app-add-movie-popup',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="popup-overlay" (click)="closePopup()">
      <div class="popup-content" (click)="$event.stopPropagation()">
        <h2>Dodaj nowy film</h2>
        <!-- Formularz dodawania filmu -->
        <form [formGroup]="movieForm" (ngSubmit)="submitForm()">
          <!-- Pola formularza -->
          <label>
            <span>Tytuł filmu:</span>
            <input formControlName="title" type="text" placeholder="Wprowadź tytuł filmu" />
          </label>
          <label>
            <span>Opis:</span>
            <textarea formControlName="description" placeholder="Wprowadź opis"></textarea>
          </label>
          <label>
            <span>Reżyser:</span>
            <input formControlName="director" type="text" placeholder="Wprowadź reżysera" />
          </label>
          <label>
            <span>Studio:</span>
            <input formControlName="studio" type="text" placeholder="Wprowadź studio" />
          </label>
          <label>
            <span>Serial/Film:</span>
            <select formControlName="type">
              <option value="Film">Film</option>
              <option value="Serial">Serial</option>
            </select>
          </label>
          <label>
            <span>Platforma:</span>
            <input formControlName="platform" type="text" placeholder="Wprowadź platformę streamingową" />
          </label>
          <label>
            <span>Gatunek:</span>
            <input formControlName="genre" type="text" placeholder="Wprowadź gatunek" />
          </label>
          <div class="button-group">
            <button type="submit" [disabled]="movieForm.invalid">Zapisz</button>
            <button type="button" (click)="closePopup()">Anuluj</button>
          </div>
        </form>
      </div>
    </div>
  `,
  styleUrls: ['./add-movie-popup.component.css'],
})
export class AddMoviePopupComponent {
  /**
   * Emituje zdarzenie zamknięcia popupu.
   * @type {EventEmitter<void>}
   */
  @Output() popupClosed = new EventEmitter<void>();

  /**
   * Emituje zdarzenie dodania nowego filmu.
   * @type {EventEmitter<any>}
   */
  @Output() movieAdded = new EventEmitter<any>();

  /**
   * Aktualny motyw aplikacji (jasny lub ciemny).
   * @type {'light' | 'dark'}
   */
  currentTheme: 'light' | 'dark' = 'dark';

  /**
   * Formularz do dodawania filmu.
   * @type {FormGroup}
   */
  movieForm: FormGroup;

  /**
   * Tworzy instancję komponentu.
   * @param {FormBuilder} fb - Usługa do tworzenia formularzy.
   * @param {HttpClient} http - Klient HTTP do obsługi żądań.
   * @param {Renderer2} renderer - Renderer do manipulacji DOM.
   */
  constructor(private fb: FormBuilder, private http: HttpClient, private renderer: Renderer2) {
    this.movieForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      director: ['', Validators.required],
      studio: ['', Validators.required],
      type: ['Film', Validators.required],
      platform: ['', Validators.required],
      genre: ['', Validators.required],
    });
  }

  /**
   * Inicjalizuje komponent i ładuje motyw aplikacji.
   */
  ngOnInit(): void {
    this.loadTheme();
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
   * Zastosowuje wybrany motyw aplikacji do popupu.
   * @private
   */
  private applyTheme(): void {
    const popupElement = document.querySelector('.popup-overlay') as HTMLElement;
    if (!popupElement) {
      console.warn('Element .popup-overlay nie istnieje w DOM.');
      return;
    }

    if (this.currentTheme === 'dark') {
      this.renderer.addClass(popupElement, 'dark-theme');
      this.renderer.removeClass(popupElement, 'light-theme');
    } else {
      this.renderer.addClass(popupElement, 'light-theme');
      this.renderer.removeClass(popupElement, 'dark-theme');
    }
  }

  /**
   * Zamyka popup i emituje zdarzenie zamknięcia.
   */
  closePopup(): void {
    this.popupClosed.emit();
  }

  /**
   * Wysyła dane formularza do API i obsługuje odpowiedź.
   */
  submitForm(): void {
    if (this.movieForm.valid) {
      const formData = this.movieForm.value;
      this.http.post<{ message: string }>('http://localhost:8080/api/library/suggest-film', formData)
        .subscribe({
          next: (response) => {
            alert(response.message);
            this.closePopup();
          },
          error: () => {
            alert('Wystąpił błąd podczas wysyłania sugestii.');
          }
        });
    }
  }
}
