import {Component, OnInit} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import { MoviePopupComponent } from '../movie-popup/movie-popup.component';
import {FilmService} from '../../services/film.service';
import {Film} from '../../services/film.service';

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
export class LibraryComponent implements OnInit {
  films: Film[] = [];
  filteredFilms: Film[] = [];
  isPopupVisible = false;
  selectedFilm: Film | null = null;

  constructor(private filmService: FilmService) {}

  ngOnInit(): void {
    this.loadFilms();
  }

  loadFilms(): void {
    this.filmService.getAllFilms().subscribe((data) => {
      this.films = data;
      this.filteredFilms = data;
    });
  }

  filterFilms(filter: string): void {
    if (filter === 'all') {
      this.loadFilms();
    } else {
      this.filmService.getFilmsByGenre(filter).subscribe((data) => {
        this.filteredFilms = data;
      });
    }
  }

  showFilmInfo(id: number): void {
    this.filmService.getFilmById(id).subscribe((film) => {
      this.selectedFilm = film;
      this.isPopupVisible = true;
    });
  }

  closePopup(): void {
    this.isPopupVisible = false;
    this.selectedFilm = null;
  }
}
