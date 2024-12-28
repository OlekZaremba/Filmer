package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Repositories.FilmsRepository;
import com.filmer.filmerbackend.Services.LibraryService;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
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

            String logoPath = "D:/Filmer/Backend/Filmer-Backend/src/main/resources/static/logo_red.png";
            try {
                ImageData imageData = ImageDataFactory.create(logoPath);
                Image logo = new Image(imageData);

                logo.scaleToFit(100, 50);

                float pageWidth = pdfDoc.getDefaultPageSize().getWidth();
                float imageWidth = logo.getImageScaledWidth();
                float marginLeft = (pageWidth - imageWidth) / 2;
                logo.setFixedPosition(marginLeft, pdfDoc.getDefaultPageSize().getHeight() - 100);

                document.add(logo);
                document.add(new Paragraph("\n\n\n"));
            } catch (Exception e) {
                System.err.println("Nie udało się załadować logo: " + e.getMessage());
            }
            Optional<Films> filmOptional = filmsRepository.findByFilmName(title);
            if (filmOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Film nie został znaleziony.".getBytes());
            }

            Films film = filmOptional.get();
            String processedTitle = replacePolishCharacters(film.getFilmName());
            String processedDescription = replacePolishCharacters(film.getFilmDesc());

            document.add(new Paragraph("Title: " + processedTitle));
            document.add(new Paragraph("Description: " + processedDescription));
            document.add(new Paragraph("Author: " + film.getDirector().getName()));
            document.add(new Paragraph("Studio: " + film.getStudio().getStudioName()));
            document.add(new Paragraph("Type: " + film.getType().getFilmType()));

            document.close();

            byte[] pdfBytes = baos.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=" + processedTitle.replaceAll("[^a-zA-Z0-9]", "_") + ".pdf");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(("Error: " + e.getMessage()).getBytes());
        }
    }


    private String replacePolishCharacters(String text) {
        return text.replace("ą", "a")
                .replace("ć", "c")
                .replace("ę", "e")
                .replace("ł", "l")
                .replace("ń", "n")
                .replace("ó", "o")
                .replace("ś", "s")
                .replace("ź", "z")
                .replace("ż", "z");
    }

    @Override
    public List<Films> getFilmsByGenre(String genreName) {
        return filmsRepository.findByGenre(genreName);
    }

    @Override
    public List<Films> getFilmsByName(String name) {
        return filmsRepository.findByFilmNameContaining(name);
    }
}
