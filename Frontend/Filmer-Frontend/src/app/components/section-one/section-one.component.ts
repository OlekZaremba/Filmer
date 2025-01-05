import { Component, AfterViewInit, ElementRef, ViewChild, Renderer2, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { MenuComponent } from '../menu/menu.component';
import { RouterModule } from '@angular/router';

/**
 * Komponent obsługujący sekcję z efektem paralaksy i dynamiczną zmianą motywu.
 * Zawiera funkcjonalność obsługi języka aplikacji oraz motywu.
 * @export
 * @class SectionOneComponent
 * @implements {AfterViewInit}
 * @implements {OnInit}
 * @implements {OnDestroy}
 */
@Component({
  selector: 'app-section-one',
  templateUrl: './section-one.component.html',
  styleUrls: ['./section-one.component.css'],
  standalone: true,
  imports: [RouterModule, MenuComponent]
})
export class SectionOneComponent implements AfterViewInit, OnInit, OnDestroy {
  /**
   * Referencja do elementu DOM z efektem paralaksy.
   * @type {ElementRef}
   */
  @ViewChild('parallaxImage') parallaxImage!: ElementRef;

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
   * @param {Renderer2} renderer - Renderer do manipulacji DOM.
   * @param {ChangeDetectorRef} cdr - Usługa do wymuszania odświeżania widoku.
   */
  constructor(private renderer: Renderer2, private cdr: ChangeDetectorRef) {}

  /**
   * Inicjalizuje komponent, ładuje motyw i język aplikacji, a także dodaje nasłuchiwanie zmian w localStorage.
   */
  ngOnInit(): void {
    this.loadTheme();
    this.loadLanguage();
    window.addEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Usuwa nasłuchiwanie zmian w localStorage po zniszczeniu komponentu.
   */
  ngOnDestroy(): void {
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  /**
   * Wywoływane po zainicjalizowaniu widoków komponentu.
   */
  ngAfterViewInit(): void {}

  /**
   * Obsługuje ruch myszy nad elementem z efektem paralaksy.
   * Dynamicznie przesuwa obraz na podstawie pozycji kursora.
   * @param {MouseEvent} event - Wydarzenie ruchu myszy.
   */
  onMouseMove(event: MouseEvent): void {
    if (this.parallaxImage && this.parallaxImage.nativeElement) {
      const rect = this.parallaxImage.nativeElement.getBoundingClientRect();
      const x = event.clientX - rect.left;
      const y = event.clientY - rect.top;

      const moveX = (x - rect.width / 2) * 0.02;
      const moveY = (y - rect.height / 2) * 0.02;

      this.parallaxImage.nativeElement.style.transform = `translate(${moveX}px, ${moveY}px)`;
    }
  }

  /**
   * Resetuje pozycję obrazu z efektem paralaksy po opuszczeniu obszaru kursorem.
   */
  onMouseLeave(): void {
    if (this.parallaxImage && this.parallaxImage.nativeElement) {
      this.parallaxImage.nativeElement.style.transform = 'translate(0, 0)';
    }
  }

  /**
   * Przełącza motyw aplikacji między jasnym a ciemnym.
   * Zapisuje wybrany motyw w localStorage.
   */
  toggleTheme(): void {
    this.currentTheme = this.currentTheme === 'dark' ? 'light' : 'dark';
    this.applyTheme(this.currentTheme);
    localStorage.setItem('theme', this.currentTheme);
  }

  /**
   * Zastosowuje wybrany motyw aplikacji do elementu DOM.
   * @private
   * @param {'light' | 'dark'} theme - Wybrany motyw aplikacji.
   */
  private applyTheme(theme: 'light' | 'dark'): void {
    if (theme === 'dark') {
      this.renderer.addClass(document.body, 'dark-theme');
      this.renderer.removeClass(document.body, 'light-theme');
    } else {
      this.renderer.addClass(document.body, 'light-theme');
      this.renderer.removeClass(document.body, 'dark-theme');
    }
  }

  /**
   * Ładuje zapisany motyw aplikacji z localStorage.
   * Jeśli motyw nie jest zapisany, domyślnie ustawia motyw ciemny.
   * @private
   */
  private loadTheme(): void {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  /**
   * Ładuje zapisany język aplikacji z localStorage.
   * Jeśli język nie jest zapisany, domyślnie ustawia język angielski.
   * @private
   */
  private loadLanguage(): void {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'english';
    this.cdr.detectChanges();
  }

  /**
   * Obsługuje zmiany w localStorage, np. zmianę języka.
   * @private
   * @param {StorageEvent} event - Wydarzenie zmiany w localStorage.
   */
  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
  }
}
