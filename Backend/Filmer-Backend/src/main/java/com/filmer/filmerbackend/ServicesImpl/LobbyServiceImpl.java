package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.*;
import com.filmer.filmerbackend.Repositories.*;
import com.filmer.filmerbackend.Services.LobbyService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LobbyServiceImpl implements LobbyService {

    private final LobbyRepository lobbyRepository;
    private final UsersRepository usersRepository;
    private final UserPreferencesRepository userPreferencesRepository;
    private final MovieSourcesRepository movieSourcesRepository;
    private final FilmGenresRepository filmGenresRepository;
    private final FilmTypeRepository filmTypeRepository;

    public LobbyServiceImpl(LobbyRepository lobbyRepository, UsersRepository usersRepository,
                            UserPreferencesRepository userPreferencesRepository, MovieSourcesRepository movieSourcesRepository,
                            FilmGenresRepository filmGenresRepository, FilmTypeRepository filmTypeRepository) {
        this.lobbyRepository = lobbyRepository;
        this.usersRepository = usersRepository;
        this.userPreferencesRepository = userPreferencesRepository;
        this.movieSourcesRepository = movieSourcesRepository;
        this.filmGenresRepository = filmGenresRepository;
        this.filmTypeRepository = filmTypeRepository;
    }

    @Override
    public Lobby createLobby(int ownerId) {
        Users owner = usersRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        Lobby lobby = new Lobby();
        lobby.setOwner(owner);
        lobby.setCreationDate(new Date());
        lobby.setActive(true);
        lobby.setLobbyCode(UUID.randomUUID().toString());
        lobby.setReady(false);
        Lobby savedLobby = lobbyRepository.save(lobby);

        return savedLobby;
    }

    @Override
    public void closeLobby(int lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        lobby.setActive(false);
        lobbyRepository.save(lobby);
    }

    @Override
    public void addUserToLobby(String lobbyCode, int userId) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        Optional<UserPreferences> existingPreferences = userPreferencesRepository.findByLobby_IdLobby(lobby.getIdLobby())
                .stream()
                .filter(p -> p.getUser().getId_user() == userId)
                .findFirst();

        if (existingPreferences.isPresent()) {
            throw new IllegalArgumentException("Użytkownik jest już w lobby.");
        }

        UserPreferences preferences = new UserPreferences();
        preferences.setLobby(lobby);
        preferences.setUser(user);
        preferences.setReady(false);
        userPreferencesRepository.save(preferences);
    }

    @Override
    public List<Users> getParticipants(String lobbyCode) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        return userPreferencesRepository.findByLobby_IdLobby(lobby.getIdLobby())
                .stream()
                .map(UserPreferences::getUser)
                .toList();
    }

    @Override
    public void saveUserPreferences(String lobbyCode, int userId, String streamingPlatform, String genre, String type) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode)
                .orElseThrow(() -> new IllegalArgumentException("Nie znaleziono lobby o podanym kodzie."));

        UserPreferences preferences = userPreferencesRepository.findByLobby_IdLobby(lobby.getIdLobby())
                .stream()
                .filter(p -> p.getUser().getId_user() == userId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje w tym lobby."));

        MovieSources platform = movieSourcesRepository.findBySourceName(streamingPlatform)
                .orElseThrow(() -> new IllegalArgumentException("Podana platforma nie istnieje: " + streamingPlatform));

        FilmGenres filmGenre = filmGenresRepository.findByGenreName(genre)
                .orElseThrow(() -> new IllegalArgumentException("Podany gatunek nie istnieje: " + genre));

        FilmType filmType = filmTypeRepository.findByFilmType(type)
                .orElseThrow(() -> new IllegalArgumentException("Podany typ filmu nie istnieje: " + type));

        preferences.setStreamingPlatform(platform);
        preferences.setGenre(filmGenre);
        preferences.setType(filmType);
        preferences.setReady(true);
        userPreferencesRepository.save(preferences);

        boolean allReady = !userPreferencesRepository.existsByLobby_IdLobbyAndIsReadyFalse(lobby.getIdLobby());
        if (allReady) {
            lobby.setReady(true);
            lobbyRepository.save(lobby);
        }
    }


    @Override
    public boolean areAllUsersReady(int lobbyId) {
        return !userPreferencesRepository.existsByLobby_IdLobbyAndIsReadyFalse(lobbyId);
    }
}
