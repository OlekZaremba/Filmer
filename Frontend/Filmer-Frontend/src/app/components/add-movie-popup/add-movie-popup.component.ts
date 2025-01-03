import {Component, EventEmitter, Output, Renderer2} from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-add-movie-popup',
  standalone: true,
  imports: [ReactiveFormsModule],
  template: `
    <div class="popup-overlay" (click)="closePopup()">
      <div class="popup-content" (click)="$event.stopPropagation()">
        <h2>Dodaj nowy film</h2>
          <form [formGroup]="movieForm" (ngSubmit)="submitForm()">
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
  @Output() popupClosed = new EventEmitter<void>();
  @Output() movieAdded = new EventEmitter<any>();
  currentTheme: 'light' | 'dark' = 'dark';


  movieForm: FormGroup;

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

  ngOnInit(): void {
    this.loadTheme();
  }

  private loadTheme(): void {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme();
  }

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

  closePopup(): void {
    this.popupClosed.emit();
  }

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
