import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SectionOneComponent} from './components/section-one/section-one.component';
import {MenuComponent} from './components/menu/menu.component';

/**
 * Główny komponent aplikacji `Filmer-Frontend`.
 * Jest odpowiedzialny za strukturę i układ aplikacji, w tym zarządzanie routami oraz integrację z głównymi komponentami, takimi jak menu i sekcja główna.
 * @export
 * @class AppComponent
 */
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SectionOneComponent, MenuComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  /**
   * Tytuł aplikacji.
   * @type {string}
   */
  title = 'Filmer-Frontend';
}
