import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class PdfService {
  private apiUrl = 'http://localhost:8080/api/library';

  constructor(private http: HttpClient) {}

  generatePdf(title: string, description: string) {
    const params = { title, description };
    return this.http.post(`${this.apiUrl}/generate-pdf`, null, {
      params,
      responseType: 'blob',
    });
  }
}
