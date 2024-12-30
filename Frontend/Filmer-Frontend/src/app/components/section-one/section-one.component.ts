import { Component, AfterViewInit, ElementRef, ViewChild, Renderer2, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { MenuComponent } from '../menu/menu.component';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-section-one',
  templateUrl: './section-one.component.html',
  styleUrls: ['./section-one.component.css'],
  standalone: true,
  imports: [RouterModule, MenuComponent]
})
export class SectionOneComponent implements AfterViewInit, OnInit, OnDestroy {
  @ViewChild('parallaxImage') parallaxImage!: ElementRef;
  currentTheme: 'light' | 'dark' = 'dark';
  currentLanguage: 'polish' | 'english' = 'english'; // Domyślny język

  constructor(private renderer: Renderer2, private cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loadTheme();
    this.loadLanguage();

    // Nasłuchiwanie zmian w localStorage
    window.addEventListener('storage', this.handleStorageChange.bind(this));
  }

  ngOnDestroy(): void {
    // Usuwanie nasłuchiwania po zniszczeniu komponentu
    window.removeEventListener('storage', this.handleStorageChange.bind(this));
  }

  ngAfterViewInit(): void {
    // Inicjalizacja, jeśli potrzebna
  }

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

  onMouseLeave(): void {
    if (this.parallaxImage && this.parallaxImage.nativeElement) {
      this.parallaxImage.nativeElement.style.transform = 'translate(0, 0)';
    }
  }

  toggleTheme() {
    this.currentTheme = this.currentTheme === 'dark' ? 'light' : 'dark';
    this.applyTheme(this.currentTheme);
    localStorage.setItem('theme', this.currentTheme);
  }

  private applyTheme(theme: 'light' | 'dark') {
    if (theme === 'dark') {
      this.renderer.addClass(document.body, 'dark-theme');
      this.renderer.removeClass(document.body, 'light-theme');
    } else {
      this.renderer.addClass(document.body, 'light-theme');
      this.renderer.removeClass(document.body, 'dark-theme');
    }
  }

  private loadTheme() {
    const savedTheme = localStorage.getItem('theme') as 'light' | 'dark';
    this.currentTheme = savedTheme || 'dark';
    this.applyTheme(this.currentTheme);
  }

  private loadLanguage() {
    const savedLanguage = localStorage.getItem('language') as 'polish' | 'english';
    this.currentLanguage = savedLanguage || 'english';
    this.cdr.detectChanges(); // Wymuszenie odświeżenia widoku
  }

  private handleStorageChange(event: StorageEvent): void {
    if (event.key === 'language') {
      this.loadLanguage();
    }
  }
}
