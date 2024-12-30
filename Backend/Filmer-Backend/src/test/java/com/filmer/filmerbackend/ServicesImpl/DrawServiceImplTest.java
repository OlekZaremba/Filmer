package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.*;
import com.filmer.filmerbackend.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DrawServiceImplTest {

    @Mock
    private FilmsRepository filmsRepository;

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private LobbyHasFilmsRepository lobbyHasFilmsRepository;

    @Mock
    private LobbyResultsRepository lobbyResultsRepository;

    @Mock
    private UsersRepository usersRepository;

    @InjectMocks
    private DrawServiceImpl drawService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void drawFilms_ShouldReturnExistingLobbyFilms_WhenLobbyHasFilms() {
        // Arrange
        String lobbyCode = "testLobby";
        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);

        Films film1 = new Films();
        film1.setIdFilm(1);
        Films film2 = new Films();
        film2.setIdFilm(2);

        LobbyHasFilms lobbyFilm1 = new LobbyHasFilms();
        lobbyFilm1.setFilm(film1);
        LobbyHasFilms lobbyFilm2 = new LobbyHasFilms();
        lobbyFilm2.setFilm(film2);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyHasFilmsRepository.findByLobby(lobby)).thenReturn(Arrays.asList(lobbyFilm1, lobbyFilm2));

        // Act
        List<Films> result = drawService.drawFilms(lobbyCode);

        // Assert
        assertEquals(2, result.size());
        assertEquals(film1, result.get(0));
        assertEquals(film2, result.get(1));
    }

    @Test
    void drawFilms_ShouldThrowException_WhenLobbyDoesNotExist() {
        // Arrange
        String lobbyCode = "nonexistentLobby";
        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> drawService.drawFilms(lobbyCode), "Lobby nie istnieje.");
    }

    @Test
    void drawFilms_ShouldReturnSelectedAndRandomFilms_WhenLobbyHasNoExistingFilms() {
        // Arrange
        String lobbyCode = "testLobby";
        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);

        // Mockowanie preferencji użytkowników
        UserPreferences pref1 = new UserPreferences();
        FilmGenres genre = new FilmGenres();
        genre.setIdGenre(1);
        pref1.setGenre(genre);

        UserPreferences pref2 = new UserPreferences();
        FilmType type = new FilmType();
        type.setIdFilmType(2);
        pref2.setType(type);

        lobby.setUserPreferences(Arrays.asList(pref1, pref2));

        // Mockowanie filmów zwróconych na podstawie preferencji
        Films selectedFilm1 = new Films();
        selectedFilm1.setIdFilm(1);

        Films selectedFilm2 = new Films();
        selectedFilm2.setIdFilm(2);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyHasFilmsRepository.findByLobby(lobby)).thenReturn(List.of());
        when(filmsRepository.findFilmsByPreferences(1, null, null)).thenReturn(List.of(selectedFilm1));
        when(filmsRepository.findFilmsByPreferences(null, 2, null)).thenReturn(List.of(selectedFilm2));

        // Mockowanie losowych filmów
        Films randomFilm = new Films();
        randomFilm.setIdFilm(3);

        when(filmsRepository.findRandomFilmsExcluding(List.of(1, 2), 14)).thenReturn(List.of(randomFilm));

        // Act
        List<Films> result = drawService.drawFilms(lobbyCode);

        // Assert
        assertEquals(3, result.size());
        verify(lobbyHasFilmsRepository, times(3)).save(any(LobbyHasFilms.class));
    }

    @Test
    void drawFilms_ShouldHandleEmptyPreferencesAndReturnRandomFilms() {
        // Arrange
        String lobbyCode = "testLobby";
        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);
        lobby.setUserPreferences(List.of());

        // Mockowanie losowych filmów
        Films randomFilm1 = new Films();
        randomFilm1.setIdFilm(1);

        Films randomFilm2 = new Films();
        randomFilm2.setIdFilm(2);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyHasFilmsRepository.findByLobby(lobby)).thenReturn(List.of());
        when(filmsRepository.findRandomFilmsExcluding(List.of(), 16)).thenReturn(List.of(randomFilm1, randomFilm2));

        // Act
        List<Films> result = drawService.drawFilms(lobbyCode);

        // Assert
        assertEquals(2, result.size());
        verify(lobbyHasFilmsRepository, times(2)).save(any(LobbyHasFilms.class));
    }

    @Test
    void submitVote_ShouldMarkVotingAsCompleted_WhenAllPlayersHaveFinished() {
        // Arrange
        String lobbyCode = "testLobby";
        Integer filmId = 1;
        Integer userId = 1;

        // Tworzenie lobby z 2 graczami w preferencjach
        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);
        lobby.setFinishedPlayersCount(1); // 1 gracz już zakończył
        lobby.setVotingCompleted(false); // Głosowanie jeszcze nie zakończone

        UserPreferences pref1 = new UserPreferences();
        pref1.setUser(new Users());
        UserPreferences pref2 = new UserPreferences();
        pref2.setUser(new Users());
        lobby.setUserPreferences(Arrays.asList(pref1, pref2)); // 2 gracze w lobby

        // Tworzenie filmu i użytkownika
        Films film = new Films();
        film.setIdFilm(filmId);

        Users user = new Users();
        user.setId_user(userId);

        // Mockowanie repozytoriów
        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(filmsRepository.findById(filmId)).thenReturn(Optional.of(film));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lobbyResultsRepository.countByLobbyAndUser(lobby, user)).thenReturn(16L); // Gracz oddał wszystkie głosy

        // Act
        drawService.submitVote(lobbyCode, filmId, userId);

        // Assert
        assertEquals(2, lobby.getFinishedPlayersCount()); // Obaj gracze zakończyli głosowanie
        assertEquals(true, lobby.getVotingCompleted()); // Głosowanie zakończone
        verify(lobbyRepository, times(2)).save(lobby); // Lobby zapisane dwa razy (dla licznika i flagi)
    }


    @Test
    void submitVote_ShouldThrowException_WhenLobbyDoesNotExist() {
        // Arrange
        String lobbyCode = "nonexistentLobby";
        Integer filmId = 1;
        Integer userId = 1;

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> drawService.submitVote(lobbyCode, filmId, userId), "Lobby nie istnieje.");
    }

    @Test
    void submitVote_ShouldThrowException_WhenFilmDoesNotExist() {
        // Arrange
        String lobbyCode = "testLobby";
        Integer filmId = 1;
        Integer userId = 1;

        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(filmsRepository.findById(filmId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> drawService.submitVote(lobbyCode, filmId, userId), "Film nie istnieje.");
    }

    @Test
    void submitVote_ShouldThrowException_WhenUserDoesNotExist() {
        // Arrange
        String lobbyCode = "testLobby";
        Integer filmId = 1;
        Integer userId = 1;

        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);

        Films film = new Films();
        film.setIdFilm(filmId);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(filmsRepository.findById(filmId)).thenReturn(Optional.of(film));
        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> drawService.submitVote(lobbyCode, filmId, userId), "Użytkownik nie istnieje.");
    }
}
