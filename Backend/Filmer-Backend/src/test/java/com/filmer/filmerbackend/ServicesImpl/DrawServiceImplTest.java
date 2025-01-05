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

/**
 * Klasa testowa dla usługi {@link DrawServiceImpl}.
 * Testuje funkcjonalności związane z losowaniem filmów oraz obsługą głosowań w lobby.
 */
public class DrawServiceImplTest {

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

    /**
     * Inicjalizacja mocków przed każdym testem.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Testuje metodę {@link DrawServiceImpl#drawFilms(String)}.
     * <p>
     * Scenariusz: Pobranie istniejących filmów przypisanych do lobby.
     * Oczekiwany wynik: Zwrócenie listy filmów z lobby.
     */
    @Test
    public void drawFilms_ShouldReturnExistingLobbyFilms_WhenLobbyHasFilms() {
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

        List<Films> result = drawService.drawFilms(lobbyCode);

        assertEquals(2, result.size());
        assertEquals(film1, result.get(0));
        assertEquals(film2, result.get(1));
    }

    /**
     * Testuje metodę {@link DrawServiceImpl#drawFilms(String)}.
     * <p>
     * Scenariusz: Próba losowania filmów dla nieistniejącego lobby.
     * Oczekiwany wynik: Rzucenie wyjątku {@link IllegalArgumentException}.
     */
    @Test
    public void drawFilms_ShouldThrowException_WhenLobbyDoesNotExist() {
        String lobbyCode = "nonexistentLobby";
        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> drawService.drawFilms(lobbyCode), "Lobby nie istnieje.");
    }

    /**
     * Testuje metodę {@link DrawServiceImpl#drawFilms(String)}.
     * <p>
     * Scenariusz: Losowanie filmów na podstawie preferencji użytkowników oraz dodanie filmów losowych.
     * Oczekiwany wynik: Zwrócenie listy filmów zawierającej filmy zgodne z preferencjami i losowe.
     */
    @Test
    public void drawFilms_ShouldReturnSelectedAndRandomFilms_WhenLobbyHasNoExistingFilms() {
        String lobbyCode = "testLobby";
        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);

        UserPreferences pref1 = new UserPreferences();
        FilmGenres genre = new FilmGenres();
        genre.setIdGenre(1);
        pref1.setGenre(genre);

        UserPreferences pref2 = new UserPreferences();
        FilmType type = new FilmType();
        type.setIdFilmType(2);
        pref2.setType(type);

        lobby.setUserPreferences(Arrays.asList(pref1, pref2));

        Films selectedFilm1 = new Films();
        selectedFilm1.setIdFilm(1);

        Films selectedFilm2 = new Films();
        selectedFilm2.setIdFilm(2);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyHasFilmsRepository.findByLobby(lobby)).thenReturn(List.of());
        when(filmsRepository.findFilmsByPreferences(1, null, null)).thenReturn(List.of(selectedFilm1));
        when(filmsRepository.findFilmsByPreferences(null, 2, null)).thenReturn(List.of(selectedFilm2));

        Films randomFilm = new Films();
        randomFilm.setIdFilm(3);

        when(filmsRepository.findRandomFilmsExcluding(List.of(1, 2), 14)).thenReturn(List.of(randomFilm));

        List<Films> result = drawService.drawFilms(lobbyCode);

        assertEquals(3, result.size());
        verify(lobbyHasFilmsRepository, times(3)).save(any(LobbyHasFilms.class));
    }

    /**
     * Testuje metodę {@link DrawServiceImpl#drawFilms(String)}.
     * <p>
     * Scenariusz: Losowanie wyłącznie losowych filmów, gdy brak preferencji użytkowników.
     * Oczekiwany wynik: Zwrócenie listy losowych filmów.
     */
    @Test
    public void drawFilms_ShouldHandleEmptyPreferencesAndReturnRandomFilms() {
        String lobbyCode = "testLobby";
        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);
        lobby.setUserPreferences(List.of());

        Films randomFilm1 = new Films();
        randomFilm1.setIdFilm(1);

        Films randomFilm2 = new Films();
        randomFilm2.setIdFilm(2);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyHasFilmsRepository.findByLobby(lobby)).thenReturn(List.of());
        when(filmsRepository.findRandomFilmsExcluding(List.of(), 16)).thenReturn(List.of(randomFilm1, randomFilm2));

        List<Films> result = drawService.drawFilms(lobbyCode);

        assertEquals(2, result.size());
        verify(lobbyHasFilmsRepository, times(2)).save(any(LobbyHasFilms.class));
    }

    /**
     * Testuje metodę {@link DrawServiceImpl#submitVote(String, Integer, Integer)}.
     * <p>
     * Scenariusz: Zakończenie głosowania po oddaniu głosów przez wszystkich graczy.
     * Oczekiwany wynik: Ustawienie flagi zakończenia głosowania i zapisanie wyników.
     */
    @Test
    public void submitVote_ShouldMarkVotingAsCompleted_WhenAllPlayersHaveFinished() {
        String lobbyCode = "testLobby";
        Integer filmId = 1;
        Integer userId = 1;

        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);
        lobby.setFinishedPlayersCount(1);
        lobby.setVotingCompleted(false);

        UserPreferences pref1 = new UserPreferences();
        pref1.setUser(new Users());
        UserPreferences pref2 = new UserPreferences();
        pref2.setUser(new Users());
        lobby.setUserPreferences(Arrays.asList(pref1, pref2));

        Films film = new Films();
        film.setIdFilm(filmId);

        Users user = new Users();
        user.setId_user(userId);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(filmsRepository.findById(filmId)).thenReturn(Optional.of(film));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(lobbyResultsRepository.countByLobbyAndUser(lobby, user)).thenReturn(16L);

        drawService.submitVote(lobbyCode, filmId, userId);

        assertEquals(2, lobby.getFinishedPlayersCount());
        assertEquals(true, lobby.getVotingCompleted());
        verify(lobbyRepository, times(2)).save(lobby);
    }

    /**
     * Testuje metodę {@link DrawServiceImpl#submitVote(String, Integer, Integer)}.
     * <p>
     * Scenariusz: Próba oddania głosu w nieistniejącym lobby.
     * Oczekiwany wynik: Rzucenie wyjątku {@link IllegalArgumentException}.
     */
    @Test
    public void submitVote_ShouldThrowException_WhenLobbyDoesNotExist() {
        String lobbyCode = "nonexistentLobby";
        Integer filmId = 1;
        Integer userId = 1;

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> drawService.submitVote(lobbyCode, filmId, userId), "Lobby nie istnieje.");
    }

    /**
     * Testuje metodę {@link DrawServiceImpl#submitVote(String, Integer, Integer)}.
     * <p>
     * Scenariusz: Próba oddania głosu na nieistniejący film.
     * Oczekiwany wynik: Rzucenie wyjątku {@link IllegalArgumentException}.
     */
    @Test
    public void submitVote_ShouldThrowException_WhenFilmDoesNotExist() {
        String lobbyCode = "testLobby";
        Integer filmId = 1;
        Integer userId = 1;

        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(filmsRepository.findById(filmId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> drawService.submitVote(lobbyCode, filmId, userId), "Film nie istnieje.");
    }

    /**
     * Testuje metodę {@link DrawServiceImpl#submitVote(String, Integer, Integer)}.
     * <p>
     * Scenariusz: Próba oddania głosu przez nieistniejącego użytkownika.
     * Oczekiwany wynik: Rzucenie wyjątku {@link IllegalArgumentException}.
     */
    @Test
    public void submitVote_ShouldThrowException_WhenUserDoesNotExist() {
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

        assertThrows(IllegalArgumentException.class, () -> drawService.submitVote(lobbyCode, filmId, userId), "Użytkownik nie istnieje.");
    }
}
