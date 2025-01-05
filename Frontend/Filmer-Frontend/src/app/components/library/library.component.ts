import {ChangeDetectorRef, Component, OnInit, Renderer2} from '@angular/core';
import {NgForOf, NgIf} from '@angular/common';
import {ReactiveFormsModule} from '@angular/forms';
import { MoviePopupComponent } from '../movie-popup/movie-popup.component';
import { AddMoviePopupComponent } from '../add-movie-popup/add-movie-popup.component';
import {FilmService} from '../../services/film.service';
import {Film} from '../../services/film.service';

/**
 * Komponent zarządzający biblioteką filmów.
 * Umożliwia filtrowanie, wyszukiwanie oraz dodawanie nowych filmów.
 * @export
 * @class LibraryComponent
 * @implements {OnInit}
 */
@Component({
  selector: 'app-library',
  standalone: true,
  imports: [
    NgForOf,
    NgIf,
    ReactiveFormsModule,
    MoviePopupComponent,
    AddMoviePopupComponent,
  ],
  templateUrl: './library.component.html',
  styleUrl: './library.component.css',
})
export class LibraryComponent implements OnInit {
  /**
   * Lista wszystkich filmów w bibliotece.
   * @type {Film[]}
   */
  films: Film[] = [];

  /**
   * Lista przefiltrowanych filmów wyświetlana użytkownikowi.
   * @type {Film[]}
   */
  filteredFilms: Film[] = [];

  /**
   * Flaga widoczności popupu z informacjami o filmie.
   * @type {boolean}
   */
  isPopupVisible = false;

  /**
   * Wybrany film do wyświetlenia w popupie.
   * @type {Film | null}
   */
  selectedFilm: Film | null = null;

  /**
   * Aktywny filtr gatunku.
   * @type {string}
   */
  activeFilter: string = 'all';

  /**
   * Flaga widoczności popupu do dodawania nowego filmu.
   * @type {boolean}
   */
  isAddPopupVisible = false;

  /**
   * Aktualny motyw aplikacji (jasny lub ciemny).
   * @type {'light' | 'dark'}
   */
  currentTheme: 'light' | 'dark' = 'dark';

  /**
   * Aktualny język aplikacji.
   * @type {'polish' | 'english'}
   */
  currentLanguage: 'polish' | 'english' = 'english';

  /**
   * Tworzy instancję komponentu.
   * @param {FilmService} filmService - Usługa do zarządzania danymi filmów.
   * @param {Renderer2} renderer - Renderer do manipulacji DOM.
   * @param {ChangeDetectorRef} cdr - Usługa wymuszająca odświeżenie widoku.
   */
  constructor(
    private filmService: FilmService,
    private renderer: Renderer2,
    private cdr: ChangeDetectorRef
  ) {}

  /**
   * Inicjalizuje komponent, ładując filmy, motyw oraz język aplikacji.
   */
  ngOnInit(): void {
    this.loadTheme();
    this.loadFilms();
    this.loadLanguage();

    // Nasłuchiwanie zmian w localStorage
    window.addEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Usuwa nasłuchiwanie zmian w localStorage po zniszczeniu komponentu.
   */
  ngOnDestroy(): void {
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Ładuje język aplikacji z localStorage.
   * @private
   */
  private loadLanguage(): void {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'english';
    this.cdr.detectChanges(); // Wymuszenie odświeżenia widoku
  }

  /**
   * Obsługuje zmiany w localStorage, np. zmiany języka.
   * @private
   * @param {StorageEvent} event - Wydarzenie zmiany w localStorage.
   */
  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
  }

  /**
   * Ładuje zapisany motyw aplikacji z localStorage.
   * @private
   */
  private loadTheme(): void {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  /**
   * Zastosowuje wybrany motyw aplikacji do elementu DOM.
   * @private
   * @param {'light' | 'dark'} theme - Wybrany motyw aplikacji.
   */
  private applyTheme(theme: 'light' | 'dark'): void {
    const container = document.querySelector('.bg') as HTMLElement;
    if (theme === 'dark') {
      this.renderer.addClass(container, 'dark-theme');
      this.renderer.removeClass(container, 'light-theme');
    } else {
      this.renderer.addClass(container, 'light-theme');
      this.renderer.removeClass(container, 'dark-theme');
    }
  }

  /**
   * Ładuje listę filmów z serwera.
   */
  loadFilms(): void {
    this.filmService.getAllFilms().subscribe((data) => {
      this.films = data;
      this.filteredFilms = data;
    });
  }

  /**
   * Filtruje listę filmów na podstawie wybranego gatunku.
   * @param {string} filter - Gatunek do filtrowania.
   */
  filterFilms(filter: string): void {
    this.activeFilter = filter;
    if (filter === 'all') {
      this.loadFilms();
    } else {
      this.filmService.getFilmsByGenre(filter).subscribe((data) => {
        this.filteredFilms = data;
      });
    }
  }

  /**
   * Wyszukuje filmy na podstawie nazwy.
   * @param {string} name - Nazwa filmu do wyszukania.
   */
  searchFilmsByName(name: string): void {
    this.filmService.getFilmsByName(name).subscribe((data) => {
      this.filteredFilms = data;
      console.log('Searched films:', data); // Debug
    });
  }

  /**
   * Wyświetla szczegóły wybranego filmu w popupie.
   * @param {number} id - Identyfikator filmu.
   */
  showFilmInfo(id: number): void {
    this.filmService.getFilmById(id).subscribe((film) => {
      this.selectedFilm = film;
      this.isPopupVisible = true;
    });
  }

  /**
   * Zamyka popup z informacjami o filmie.
   */
  closePopup(): void {
    this.isPopupVisible = false;
    this.selectedFilm = null;
  }

  /**
   * Otwiera popup do dodawania nowego filmu.
   */
  openAddPopup(): void {
    this.isAddPopupVisible = true;
  }

  /**
   * Zamyka popup do dodawania nowego filmu.
   */
  closeAddPopup(): void {
    this.isAddPopupVisible = false;
  }

  /**
   * Obsługuje zdarzenie dodania nowego filmu.
   * @param {any} movie - Obiekt nowo dodanego filmu.
   */
  handleMovieAdded(movie: any): void {
    console.log('Dodano film:', movie);
    this.closeAddPopup();
  }
}
