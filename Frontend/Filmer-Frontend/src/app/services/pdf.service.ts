import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

/**
 * Serwis PdfService odpowiedzialny za generowanie plików PDF z informacjami o filmach.
 * @export
 * @class PdfService
 */
@Injectable({
  providedIn: 'root',
})
export class PdfService {
  /**
   * URL API do generowania plików PDF.
   * @private
   * @type {string}
   */
  private apiUrl = 'http://localhost:8080/api/library';

  /**
   * Tworzy instancję PdfService.
   * @param {HttpClient} http Serwis do wykonywania żądań HTTP.
   */
  constructor(private http: HttpClient) {}

  /**
   * Generuje plik PDF z podanym tytułem i opisem.
   * @param {string} title Tytuł filmu do umieszczenia w PDF.
   * @param {string} description Opis filmu do umieszczenia w PDF.
   * @returns {Observable<Blob>} Observable zawierający wygenerowany plik PDF jako Blob.
   */
  generatePdf(title: string, description: string) {
    const params = { title, description };
    return this.http.post(`${this.apiUrl}/generate-pdf`, null, {
      params,
      responseType: 'blob',
    });
  }
}
