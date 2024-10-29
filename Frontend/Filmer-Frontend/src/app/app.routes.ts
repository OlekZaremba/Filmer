import { Routes } from '@angular/router';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { SectionOneComponent } from './components/section-one/section-one.component';

export const routes: Routes = [
  { path: '', component: SectionOneComponent },      // Domyślna strona główna
  { path: 'login', component: LoginPageComponent }   // Trasa do komponentu "PROFIL"
];
