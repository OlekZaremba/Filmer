package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.*;
import com.filmer.filmerbackend.Repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LobbyServiceImplTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserPreferencesRepository userPreferencesRepository;

    @Mock
    private MovieSourcesRepository movieSourcesRepository;

    @Mock
    private FilmGenresRepository filmGenresRepository;

    @Mock
    private FilmTypeRepository filmTypeRepository;

    @InjectMocks
    private LobbyServiceImpl lobbyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createLobby_ShouldCreateAndReturnLobby() {
        int ownerId = 1;
        Users owner = new Users();
        owner.setId_user(ownerId);

        Lobby lobby = new Lobby();
        lobby.setIdLobby(1);
        lobby.setOwner(owner);

        when(usersRepository.findById(ownerId)).thenReturn(Optional.of(owner));
        when(lobbyRepository.save(any(Lobby.class))).thenReturn(lobby);

        Lobby result = lobbyService.createLobby(ownerId);

        assertNotNull(result);
        assertEquals(owner, result.getOwner());
        verify(usersRepository, times(1)).findById(ownerId);
        verify(lobbyRepository, times(1)).save(any(Lobby.class));
    }

    @Test
    void closeLobby_ShouldDeactivateLobby() {
        int lobbyId = 1;
        Lobby lobby = new Lobby();
        lobby.setIdLobby(lobbyId);
        lobby.setActive(true);

        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));

        lobbyService.closeLobby(lobbyId);

        assertFalse(lobby.isActive());
        verify(lobbyRepository, times(1)).save(lobby);
    }

    @Test
    void addUserToLobby_ShouldAddUserToLobby() {
        String lobbyCode = "testCode";
        int userId = 1;

        Lobby lobby = new Lobby();
        lobby.setIdLobby(1);

        Users user = new Users();
        user.setId_user(userId);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userPreferencesRepository.findByLobby_IdLobby(lobby.getIdLobby())).thenReturn(List.of());

        lobbyService.addUserToLobby(lobbyCode, userId);

        verify(userPreferencesRepository, times(1)).save(any(UserPreferences.class));
    }

    @Test
    void getParticipants_ShouldReturnParticipants() {
        String lobbyCode = "testCode";

        Lobby lobby = new Lobby();
        lobby.setIdLobby(1);

        Users user1 = new Users();
        user1.setId_user(1);
        Users user2 = new Users();
        user2.setId_user(2);

        UserPreferences pref1 = new UserPreferences();
        pref1.setUser(user1);
        UserPreferences pref2 = new UserPreferences();
        pref2.setUser(user2);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(userPreferencesRepository.findByLobby_IdLobby(lobby.getIdLobby())).thenReturn(List.of(pref1, pref2));

        List<Users> participants = lobbyService.getParticipants(lobbyCode);

        assertEquals(2, participants.size());
        verify(userPreferencesRepository, times(1)).findByLobby_IdLobby(lobby.getIdLobby());
    }

    @Test
    void saveUserPreferences_ShouldUpdatePreferences_WhenValidInput() {
        String lobbyCode = "testLobbyCode";
        int userId = 1;
        String streamingPlatform = "Netflix";
        String genre = "Action";
        String type = "Movie";

        Lobby lobby = new Lobby();
        lobby.setIdLobby(1);

        Users user = new Users();
        user.setId_user(userId);

        UserPreferences preferences = new UserPreferences();
        preferences.setUser(user);
        preferences.setLobby(lobby);

        MovieSources platform = new MovieSources();
        platform.setSourceName(streamingPlatform);

        FilmGenres filmGenre = new FilmGenres();
        filmGenre.setGenreName(genre);

        FilmType filmType = new FilmType();
        filmType.setFilmType(type);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(userPreferencesRepository.findByLobby_IdLobby(lobby.getIdLobby())).thenReturn(List.of(preferences));
        when(movieSourcesRepository.findBySourceName(streamingPlatform)).thenReturn(Optional.of(platform));
        when(filmGenresRepository.findByGenreName(genre)).thenReturn(Optional.of(filmGenre));
        when(filmTypeRepository.findByFilmType(type)).thenReturn(Optional.of(filmType));
        when(userPreferencesRepository.existsByLobby_IdLobbyAndIsReadyFalse(lobby.getIdLobby())).thenReturn(false);

        lobbyService.saveUserPreferences(lobbyCode, userId, streamingPlatform, genre, type);

        assertEquals(platform, preferences.getStreamingPlatform());
        assertEquals(filmGenre, preferences.getGenre());
        assertEquals(filmType, preferences.getType());
        assertTrue(preferences.isReady());
        verify(userPreferencesRepository, times(1)).save(preferences);
        verify(lobbyRepository, times(1)).save(lobby);
    }

    @Test
    void finishVoting_ShouldSetVotingCompleted_WhenAllPlayersFinish() {
        String lobbyCode = "testLobbyCode";
        int userId = 1;

        Lobby lobby = new Lobby();
        lobby.setIdLobby(1);
        lobby.setFinishedPlayersCount(1);

        Users user1 = new Users();
        user1.setId_user(1);
        Users user2 = new Users();
        user2.setId_user(2);

        UserPreferences pref1 = new UserPreferences();
        pref1.setUser(user1);
        pref1.setLobby(lobby);

        UserPreferences pref2 = new UserPreferences();
        pref2.setUser(user2);
        pref2.setLobby(lobby);

        lobby.setUserPreferences(List.of(pref1, pref2));

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyRepository.save(any(Lobby.class))).thenReturn(lobby);

        lobbyService.finishVoting(lobbyCode, userId);

        assertEquals(2, lobby.getFinishedPlayersCount());
        assertTrue(lobby.getVotingCompleted());
        verify(lobbyRepository, times(1)).save(lobby);
    }

    @Test
    void areAllUsersReady_ShouldReturnTrue_WhenAllUsersAreReady() {
        int lobbyId = 1;

        when(userPreferencesRepository.existsByLobby_IdLobbyAndIsReadyFalse(lobbyId)).thenReturn(false);

        boolean result = lobbyService.areAllUsersReady(lobbyId);

        assertTrue(result);
        verify(userPreferencesRepository, times(1)).existsByLobby_IdLobbyAndIsReadyFalse(lobbyId);
    }

    @Test
    void startGame_ShouldSetStarted_WhenLobbyIsActiveAndAllUsersReady() {
        int lobbyId = 1;

        Lobby lobby = new Lobby();
        lobby.setIdLobby(lobbyId);
        lobby.setActive(true);

        when(lobbyRepository.findById(lobbyId)).thenReturn(Optional.of(lobby));
        when(userPreferencesRepository.existsByLobby_IdLobbyAndIsReadyFalse(lobbyId)).thenReturn(false);

        lobbyService.startGame(lobbyId);

        assertTrue(lobby.isStarted());
        verify(lobbyRepository, times(1)).save(lobby);
    }

    @Test
    void checkVotingCompletion_ShouldReturnTrue_WhenVotingIsCompleted() {
        String lobbyCode = "testLobbyCode";

        Lobby lobby = new Lobby();
        lobby.setLobbyCode(lobbyCode);
        lobby.setVotingCompleted(true);

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));

        boolean result = lobbyService.checkVotingCompletion(lobbyCode);

        assertTrue(result);
        verify(lobbyRepository, times(1)).findByLobbyCode(lobbyCode);
    }
}
