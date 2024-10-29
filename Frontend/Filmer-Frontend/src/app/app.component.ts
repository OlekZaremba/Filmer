import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SectionOneComponent} from './components/section-one/section-one.component';
import {MenuComponent} from './components/menu/menu.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SectionOneComponent, MenuComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Filmer-Frontend';
}
