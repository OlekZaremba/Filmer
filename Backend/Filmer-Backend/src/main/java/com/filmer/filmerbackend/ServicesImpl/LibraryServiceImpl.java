package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Repositories.FilmsRepository;
import com.filmer.filmerbackend.Services.LibraryService;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryServiceImpl implements LibraryService {

    private final FilmsRepository filmsRepository;
    @Override
    public Optional<Films> getFilmById(Integer idFilm) {
        return filmsRepository.findById(idFilm);
    }

    @Override
    public List<Films> getAllFilms() {
        return filmsRepository.findAll();
    }

    @Override
    public ResponseEntity<byte[]> generatePdf(String title, String description) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("Tytuł: " + title));
            document.add(new Paragraph("Opis: " + description));
            document.close();

            byte[] pdfBytes = baos.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + title + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
