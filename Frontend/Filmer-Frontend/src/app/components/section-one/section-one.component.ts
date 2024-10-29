import {Component, AfterViewInit, ElementRef, ViewChild} from '@angular/core';
import {MenuComponent} from '../menu/menu.component';

@Component({
  selector: 'app-section-one',
  templateUrl: './section-one.component.html',
  styleUrl: './section-one.component.css',
  standalone: true,
  imports: [MenuComponent]
})
export class SectionOneComponent implements AfterViewInit {
  @ViewChild('parallaxImage') parallaxImage!: ElementRef;

  constructor() { }

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
}
