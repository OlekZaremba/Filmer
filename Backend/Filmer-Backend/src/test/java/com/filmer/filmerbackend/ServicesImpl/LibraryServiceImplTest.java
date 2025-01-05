package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.*;
import com.filmer.filmerbackend.Repositories.FilmsRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import com.filmer.filmerbackend.Repositories.WatchedMoviesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Klasa testowa dla usługi {@link LibraryServiceImpl}.
 * Testuje funkcjonalności zarządzania biblioteką filmów, w tym wyszukiwanie filmów,
 * generowanie plików PDF, zarządzanie ocenami i wysyłanie sugestii filmowych.
 */
public class LibraryServiceImplTest {

    @Mock
    private FilmsRepository filmsRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private WatchedMoviesRepository watchedMoviesRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private LibraryServiceImpl libraryService;

    /**
     * Inicjalizacja mocków przed każdym testem.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#getFilmById(Integer)}.
     * <p>
     * Scenariusz: Pobranie filmu po jego identyfikatorze.
     * Oczekiwany wynik: Zwrócenie obiektu filmu, jeśli istnieje.
     */
    @Test
    public void getFilmById_ShouldReturnFilm_WhenFilmExists() {
        Integer filmId = 1;
        Films film = new Films();
        film.setIdFilm(filmId);

        when(filmsRepository.findById(filmId)).thenReturn(Optional.of(film));

        Optional<Films> result = libraryService.getFilmById(filmId);

        assertTrue(result.isPresent());
        assertEquals(film, result.get());
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#getFilmById(Integer)}.
     * <p>
     * Scenariusz: Próba pobrania filmu po identyfikatorze, gdy film nie istnieje.
     * Oczekiwany wynik: Zwrócenie pustego {@link Optional}.
     */
    @Test
    public void getFilmById_ShouldReturnEmptyOptional_WhenFilmDoesNotExist() {
        Integer filmId = 1;

        when(filmsRepository.findById(filmId)).thenReturn(Optional.empty());

        Optional<Films> result = libraryService.getFilmById(filmId);

        assertTrue(result.isEmpty());
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#getAllFilms()}.
     * <p>
     * Scenariusz: Pobranie wszystkich filmów z biblioteki.
     * Oczekiwany wynik: Zwrócenie listy filmów.
     */
    @Test
    public void getAllFilms_ShouldReturnListOfFilms() {
        Films film1 = new Films();
        Films film2 = new Films();
        when(filmsRepository.findAll()).thenReturn(List.of(film1, film2));

        List<Films> result = libraryService.getAllFilms();

        assertEquals(2, result.size());
        assertEquals(film1, result.get(0));
        assertEquals(film2, result.get(1));
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#generatePdf(String, String)}.
     * <p>
     * Scenariusz: Generowanie pliku PDF z informacjami o istniejącym filmie.
     * Oczekiwany wynik: Zwrócenie odpowiedzi HTTP z zawartością PDF i statusem 200.
     */
    @Test
    public void generatePdf_ShouldReturnPdfBytes_WhenFilmExists() {
        String title = "Test Film";
        String description = "Test Description";

        Films film = new Films();
        film.setFilmName(title);
        film.setFilmDesc(description);

        FilmStudio studio = new FilmStudio();
        studio.setStudioName("Test Studio");
        film.setStudio(studio);

        FilmType type = new FilmType();
        type.setFilmType("Movie");
        film.setType(type);

        when(filmsRepository.findByFilmName(title)).thenReturn(Optional.of(film));

        ResponseEntity<byte[]> response = libraryService.generatePdf(title, description);

        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(filmsRepository, times(1)).findByFilmName(title);
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#generatePdf(String, String)}.
     * <p>
     * Scenariusz: Próba wygenerowania pliku PDF dla nieistniejącego filmu.
     * Oczekiwany wynik: Zwrócenie odpowiedzi HTTP z komunikatem błędu i statusem 400.
     */
    @Test
    public void generatePdf_ShouldReturnBadRequest_WhenFilmDoesNotExist() {
        String title = "Nonexistent Film";
        String description = "Some Description";

        when(filmsRepository.findByFilmName(title)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = libraryService.generatePdf(title, description);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Film nie został znaleziony.", new String(response.getBody()));
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#sendFilmSuggestion(String, String, String, String, String, String, String)}.
     * <p>
     * Scenariusz: Wysłanie sugestii filmowej przez e-mail.
     * Oczekiwany wynik: Wywołanie wysyłki e-maila.
     */
    @Test
    public void sendFilmSuggestion_ShouldSendEmail() {
        String title = "Film Title";
        String description = "Film Description";
        String genre = "Action";
        String platform = "Netflix";
        String director = "Director Name";
        String studio = "Studio Name";
        String type = "Movie";

        libraryService.sendFilmSuggestion(title, description, genre, platform, director, studio, type);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#getRating(Integer, Integer)}.
     * <p>
     * Scenariusz: Pobranie oceny filmu wystawionej przez użytkownika.
     * Oczekiwany wynik: Zwrócenie oceny, jeśli istnieje.
     */
    @Test
    public void getRating_ShouldReturnRating_WhenExists() {
        Integer filmId = 1;
        Integer userId = 1;
        WatchedMovies watchedMovie = new WatchedMovies();
        watchedMovie.setRating(5);

        when(watchedMoviesRepository.findByFilmIdAndUserId(filmId, userId)).thenReturn(Optional.of(watchedMovie));

        Optional<Integer> rating = libraryService.getRating(filmId, userId);

        assertTrue(rating.isPresent());
        assertEquals(5, rating.get());
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#getRating(Integer, Integer)}.
     * <p>
     * Scenariusz: Próba pobrania oceny, gdy nie istnieje.
     * Oczekiwany wynik: Zwrócenie pustego {@link Optional}.
     */
    @Test
    public void getRating_ShouldReturnEmptyOptional_WhenNotExists() {
        Integer filmId = 1;
        Integer userId = 1;

        when(watchedMoviesRepository.findByFilmIdAndUserId(filmId, userId)).thenReturn(Optional.empty());

        Optional<Integer> rating = libraryService.getRating(filmId, userId);

        assertTrue(rating.isEmpty());
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#setRating(Integer, Integer, Integer)}.
     * <p>
     * Scenariusz: Aktualizacja oceny filmu, gdy obiekt istnieje w bazie danych.
     * Oczekiwany wynik: Zapisanie nowej oceny w bazie danych.
     */
    @Test
    public void setRating_ShouldSaveRating_WhenWatchedMovieExists() {
        Integer filmId = 1;
        Integer userId = 1;
        Integer rating = 4;

        WatchedMovies watchedMovie = new WatchedMovies();
        when(watchedMoviesRepository.findByFilmIdAndUserId(filmId, userId)).thenReturn(Optional.of(watchedMovie));

        libraryService.setRating(filmId, userId, rating);

        verify(watchedMoviesRepository, times(1)).save(watchedMovie);
        assertEquals(rating, watchedMovie.getRating());
    }

    /**
     * Testuje metodę {@link LibraryServiceImpl#setRating(Integer, Integer, Integer)}.
     * <p>
     * Scenariusz: Ustawienie oceny filmu dla użytkownika, gdy brak odpowiedniego rekordu w bazie.
     * Oczekiwany wynik: Utworzenie nowego obiektu {@link WatchedMovies} i zapisanie go w bazie danych.
     */
    @Test
    public void setRating_ShouldCreateAndSaveWatchedMovie_WhenNotExists() {
        Integer filmId = 1;
        Integer userId = 1;
        Integer rating = 5;

        Films film = new Films();
        Users user = new Users();

        when(watchedMoviesRepository.findByFilmIdAndUserId(filmId, userId)).thenReturn(Optional.empty());
        when(filmsRepository.findById(filmId)).thenReturn(Optional.of(film));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        libraryService.setRating(filmId, userId, rating);

        verify(watchedMoviesRepository, times(1)).save(any(WatchedMovies.class));
    }
}
