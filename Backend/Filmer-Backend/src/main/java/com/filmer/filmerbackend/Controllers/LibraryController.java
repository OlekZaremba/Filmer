package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Dtos.FilmSuggestionRequest;
import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Services.LibraryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/library")
public class LibraryController {

    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    @GetMapping("/films/{id}")
    public ResponseEntity<Films> getFilmById(@PathVariable Integer id) {
        Optional<Films> film = libraryService.getFilmById(id);
        return film.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(@RequestParam String title, @RequestParam String description) {
        return libraryService.generatePdf(title, description);
    }


    @GetMapping("/films")
    public ResponseEntity<List<Films>> getAllFilms() {
        List<Films> films = libraryService.getAllFilms();
        return ResponseEntity.ok(films);
    }

    @GetMapping("/films/filter")
    public ResponseEntity<List<Films>> getFilmsByGenre(@RequestParam String genre) {
        List<Films> filteredFilms = libraryService.getFilmsByGenre(genre);
        return ResponseEntity.ok(filteredFilms);
    }

    @GetMapping("/films/search")
    public ResponseEntity<List<Films>> getFilmsByName(@RequestParam String name) {
        List<Films> films = libraryService.getFilmsByName(name);
        return ResponseEntity.ok(films);
    }

    @PostMapping("/suggest-film")
    public ResponseEntity<Map<String, String>> suggestFilm(@RequestBody FilmSuggestionRequest request) {
        libraryService.sendFilmSuggestion(
                request.getTitle(),
                request.getDescription(),
                request.getGenre(),
                request.getPlatform(),
                request.getDirector(),
                request.getStudio(),
                request.getType()
        );

        Map<String, String> response = new HashMap<>();
        response.put("message", "Sugestia została wysłana pomyślnie.");
        return ResponseEntity.ok(response);
    }

    @GetMapping("films/watched-movies/{filmId}/rating")
    public ResponseEntity<Integer> getRating(@PathVariable Integer filmId, @RequestParam Integer userId) {
        return libraryService.getRating(filmId, userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("films/watched-movies/{filmId}/rating")
    public ResponseEntity<Map<String, Object>> setRating(
            @PathVariable Integer filmId,
            @RequestParam Integer userId,
            @RequestParam Integer rating) {
        libraryService.setRating(filmId, userId, rating);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Ocena została zapisana pomyślnie.");
        response.put("filmId", filmId);
        response.put("userId", userId);
        response.put("rating", rating);

        return ResponseEntity.ok(response);
    }

}
