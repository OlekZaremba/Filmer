package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Dtos.FilmSuggestionRequest;
import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Services.LibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LibraryControllerTest {

    private final LibraryService libraryService = mock(LibraryService.class);
    private final LibraryController libraryController = new LibraryController(libraryService);

    @Test
    void dummyGetFilmByIdTest() {
        when(libraryService.getFilmById(1)).thenReturn(Optional.of(new Films()));
        libraryController.getFilmById(1);
    }

    @Test
    void dummyGeneratePdfTest() {
        when(libraryService.generatePdf("title", "description")).thenReturn(ResponseEntity.ok(new byte[0]));
        libraryController.generatePdf("title", "description");
    }

    @Test
    void dummyGetAllFilmsTest() {
        when(libraryService.getAllFilms()).thenReturn(Collections.emptyList());
        libraryController.getAllFilms();
    }

    @Test
    void dummyGetFilmsByGenreTest() {
        when(libraryService.getFilmsByGenre("genre")).thenReturn(Collections.emptyList());
        libraryController.getFilmsByGenre("genre");
    }

    @Test
    void dummyGetFilmsByNameTest() {
        when(libraryService.getFilmsByName("name")).thenReturn(Collections.emptyList());
        libraryController.getFilmsByName("name");
    }

    @Test
    void dummySuggestFilmTest() {
        FilmSuggestionRequest request = new FilmSuggestionRequest();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Sugestia została wysłana pomyślnie.");
        libraryController.suggestFilm(request);
    }

    @Test
    void dummyGetRatingTest() {
        when(libraryService.getRating(1, 1)).thenReturn(Optional.of(5));
        libraryController.getRating(1, 1);
    }

    @Test
    void dummySetRatingTest() {
        libraryController.setRating(1, 1, 5);
    }
}
