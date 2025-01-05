import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';

import { routes } from './app.routes';

/**
 * Konfiguracja aplikacji Angular zawierająca definicję routingu,
 * klienta HTTP oraz ustawienia wykrywania zmian strefy.
 * @export
 * @const appConfig
 * @type {ApplicationConfig}
 */
export const appConfig: ApplicationConfig = {
  providers: [
    /**
     * Zapewnia konfigurację wykrywania zmian strefy z włączonym scalaniem zdarzeń
     * (`eventCoalescing`), co może poprawić wydajność aplikacji w przypadku dużej liczby zdarzeń.
     */
    provideZoneChangeDetection({ eventCoalescing: true }),

    /**
     * Zapewnia konfigurację routingu aplikacji, korzystając z definicji tras `routes`.
     */
    provideRouter(routes),

    /**
     * Zapewnia klienta HTTP do wykonywania żądań HTTP w aplikacji.
     */
    provideHttpClient()
  ]
};
