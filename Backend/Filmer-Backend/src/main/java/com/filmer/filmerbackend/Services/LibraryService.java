package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Films;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface LibraryService {
    Optional<Films> getFilmById(Integer idFilm);
    ResponseEntity<byte[]> generatePdf(String title, String description);
    List<Films> getAllFilms();
    List<Films> getFilmsByGenre(String genreName);
    List<Films> getFilmsByName(String name);
}
