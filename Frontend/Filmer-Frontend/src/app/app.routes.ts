import { Routes } from '@angular/router';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { SectionOneComponent } from './components/section-one/section-one.component';
import {UserProfileComponent} from './components/user-profile/user-profile.component';
import {LobbyComponent} from './components/lobby/lobby.component';
import {DrawComponent} from './components/draw/draw.component';
import {ResultsComponent} from './components/results/results.component';
import {LibraryComponent} from './components/library/library.component';

export const routes: Routes = [
  { path: '', component: SectionOneComponent },
  { path: 'login', component: LoginPageComponent },
  { path: 'library', component: LibraryComponent },
  { path: 'user-profile', component: UserProfileComponent},
  { path: 'create-lobby', component: LobbyComponent},
  { path: 'create-lobby2', component: LobbyComponent},
  { path: 'lobby/:lobbyCode', component: LobbyComponent },
  { path: 'draw/:lobbyCode', component: DrawComponent},
  { path: 'results/:lobbyCode', component: ResultsComponent }
];
