import { Routes } from '@angular/router';
import { LoginPageComponent } from './components/login-page/login-page.component';
import { SectionOneComponent } from './components/section-one/section-one.component';
import {UserProfileComponent} from './components/user-profile/user-profile.component';
import {LobbyComponent} from './components/lobby/lobby.component';
import {DrawComponent} from './components/draw/draw.component';
import {ResultsComponent} from './components/results/results.component';
import {LibraryComponent} from './components/library/library.component';

/**
 * Definicja tras aplikacji Angular.
 * Zawiera mapowanie ścieżek URL na odpowiednie komponenty.
 * @export
 * @const routes
 * @type {Routes}
 */
export const routes: Routes = [
  /**
   * Główna strona aplikacji. Ładuje komponent `SectionOneComponent`.
   * @path ''
   */
  { path: '', component: SectionOneComponent },

  /**
   * Strona logowania użytkownika. Ładuje komponent `LoginPageComponent`.
   * @path 'login'
   */
  { path: 'login', component: LoginPageComponent },

  /**
   * Strona biblioteki filmów. Ładuje komponent `LibraryComponent`.
   * @path 'library'
   */
  { path: 'library', component: LibraryComponent },

  /**
   * Strona profilu użytkownika. Ładuje komponent `UserProfileComponent`.
   * @path 'user-profile'
   */
  { path: 'user-profile', component: UserProfileComponent },

  /**
   * Strona tworzenia lobby (pierwsza wersja). Ładuje komponent `LobbyComponent`.
   * @path 'create-lobby'
   */
  { path: 'create-lobby', component: LobbyComponent },

  /**
   * Strona tworzenia lobby (druga wersja). Ładuje komponent `LobbyComponent`.
   * @path 'create-lobby2'
   */
  { path: 'create-lobby2', component: LobbyComponent },

  /**
   * Strona lobby o określonym kodzie. Ładuje komponent `LobbyComponent`.
   * @path 'lobby/:lobbyCode'
   * @param {string} lobbyCode Kod lobby.
   */
  { path: 'lobby/:lobbyCode', component: LobbyComponent },

  /**
   * Strona losowania filmów w określonym lobby. Ładuje komponent `DrawComponent`.
   * @path 'draw/:lobbyCode'
   * @param {string} lobbyCode Kod lobby.
   */
  { path: 'draw/:lobbyCode', component: DrawComponent },

  /**
   * Strona wyników głosowania w określonym lobby. Ładuje komponent `ResultsComponent`.
   * @path 'results/:lobbyCode'
   * @param {string} lobbyCode Kod lobby.
   */
  { path: 'results/:lobbyCode', component: ResultsComponent }
];
