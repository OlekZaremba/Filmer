import { Component } from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import { MoviePopupComponent } from '../movie-popup/movie-popup.component';

@Component({
  selector: 'app-library',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    MoviePopupComponent
  ],
  templateUrl: './library.component.html',
  styleUrl: './library.component.css'
})
export class LibraryComponent {  isPopupVisible = false;
  selectedMovieTitle = 'Tytuł filmu';
  selectedMovieDescription = 'Opis filmu.';

  showMovieInfo(title: string, description: string): void {
    this.selectedMovieTitle = title;
    this.selectedMovieDescription = description;
    this.isPopupVisible = true;
  }

  closePopup(): void {
    this.isPopupVisible = false;
  }
}
