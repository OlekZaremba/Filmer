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

class LibraryServiceImplTest {

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFilmById_ShouldReturnFilm_WhenFilmExists() {
        // Arrange
        Integer filmId = 1;
        Films film = new Films();
        film.setIdFilm(filmId);

        when(filmsRepository.findById(filmId)).thenReturn(Optional.of(film));

        // Act
        Optional<Films> result = libraryService.getFilmById(filmId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(film, result.get());
    }

    @Test
    void getFilmById_ShouldReturnEmptyOptional_WhenFilmDoesNotExist() {
        // Arrange
        Integer filmId = 1;

        when(filmsRepository.findById(filmId)).thenReturn(Optional.empty());

        // Act
        Optional<Films> result = libraryService.getFilmById(filmId);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void getAllFilms_ShouldReturnListOfFilms() {
        // Arrange
        Films film1 = new Films();
        Films film2 = new Films();
        when(filmsRepository.findAll()).thenReturn(List.of(film1, film2));

        // Act
        List<Films> result = libraryService.getAllFilms();

        // Assert
        assertEquals(2, result.size());
        assertEquals(film1, result.get(0));
        assertEquals(film2, result.get(1));
    }

    @Test
    void generatePdf_ShouldReturnPdfBytes_WhenFilmExists() {
        // Arrange
        String title = "Test Film";
        String description = "Test Description";

        Films film = new Films();
        film.setFilmName(title);
        film.setFilmDesc(description);

        // Mockowanie FilmStudio
        FilmStudio studio = new FilmStudio();
        studio.setStudioName("Test Studio");
        film.setStudio(studio);

        // Mockowanie FilmType
        FilmType type = new FilmType();
        type.setFilmType("Movie");
        film.setType(type);

        when(filmsRepository.findByFilmName(title)).thenReturn(Optional.of(film));

        // Act
        ResponseEntity<byte[]> response = libraryService.generatePdf(title, description);

        // Assert
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(filmsRepository, times(1)).findByFilmName(title);
    }


    @Test
    void generatePdf_ShouldReturnBadRequest_WhenFilmDoesNotExist() {
        // Arrange
        String title = "Nonexistent Film";
        String description = "Some Description";

        when(filmsRepository.findByFilmName(title)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<byte[]> response = libraryService.generatePdf(title, description);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Film nie został znaleziony.", new String(response.getBody()));
    }

    @Test
    void sendFilmSuggestion_ShouldSendEmail() {
        // Arrange
        String title = "Film Title";
        String description = "Film Description";
        String genre = "Action";
        String platform = "Netflix";
        String director = "Director Name";
        String studio = "Studio Name";
        String type = "Movie";

        // Act
        libraryService.sendFilmSuggestion(title, description, genre, platform, director, studio, type);

        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void getRating_ShouldReturnRating_WhenExists() {
        // Arrange
        Integer filmId = 1;
        Integer userId = 1;
        WatchedMovies watchedMovie = new WatchedMovies();
        watchedMovie.setRating(5);

        when(watchedMoviesRepository.findByFilmIdAndUserId(filmId, userId)).thenReturn(Optional.of(watchedMovie));

        // Act
        Optional<Integer> rating = libraryService.getRating(filmId, userId);

        // Assert
        assertTrue(rating.isPresent());
        assertEquals(5, rating.get());
    }

    @Test
    void getRating_ShouldReturnEmptyOptional_WhenNotExists() {
        // Arrange
        Integer filmId = 1;
        Integer userId = 1;

        when(watchedMoviesRepository.findByFilmIdAndUserId(filmId, userId)).thenReturn(Optional.empty());

        // Act
        Optional<Integer> rating = libraryService.getRating(filmId, userId);

        // Assert
        assertTrue(rating.isEmpty());
    }

    @Test
    void setRating_ShouldSaveRating_WhenWatchedMovieExists() {
        // Arrange
        Integer filmId = 1;
        Integer userId = 1;
        Integer rating = 4;

        WatchedMovies watchedMovie = new WatchedMovies();
        when(watchedMoviesRepository.findByFilmIdAndUserId(filmId, userId)).thenReturn(Optional.of(watchedMovie));

        // Act
        libraryService.setRating(filmId, userId, rating);

        // Assert
        verify(watchedMoviesRepository, times(1)).save(watchedMovie);
        assertEquals(rating, watchedMovie.getRating());
    }

    @Test
    void setRating_ShouldCreateAndSaveWatchedMovie_WhenNotExists() {
        // Arrange
        Integer filmId = 1;
        Integer userId = 1;
        Integer rating = 5;

        Films film = new Films();
        Users user = new Users();

        when(watchedMoviesRepository.findByFilmIdAndUserId(filmId, userId)).thenReturn(Optional.empty());
        when(filmsRepository.findById(filmId)).thenReturn(Optional.of(film));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        // Act
        libraryService.setRating(filmId, userId, rating);

        // Assert
        verify(watchedMoviesRepository, times(1)).save(any(WatchedMovies.class));
    }
}
