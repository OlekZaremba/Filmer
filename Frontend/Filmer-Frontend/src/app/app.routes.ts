import { Routes } from '@angular/router';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { SectionOneComponent } from './components/section-one/section-one.component';
import {UserProfileComponent} from './components/user-profile/user-profile.component';

export const routes: Routes = [
  { path: '', component: SectionOneComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'user-profile', component: UserProfileComponent}
];
