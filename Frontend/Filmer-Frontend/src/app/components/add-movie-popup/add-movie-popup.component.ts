import { Component, EventEmitter, Output } from '@angular/core';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';

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

  movieForm: FormGroup;

  constructor(private fb: FormBuilder) {
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

  closePopup(): void {
    this.popupClosed.emit();
  }

  submitForm(): void {
    if (this.movieForm.valid) {
      this.movieAdded.emit(this.movieForm.value);
      this.closePopup();
    }
  }
}
