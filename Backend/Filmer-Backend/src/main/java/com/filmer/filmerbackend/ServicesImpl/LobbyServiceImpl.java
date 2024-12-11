package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.UserPreferences;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.LobbyRepository;
import com.filmer.filmerbackend.Repositories.UserPreferencesRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
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

    public LobbyServiceImpl(LobbyRepository lobbyRepository, UsersRepository usersRepository, UserPreferencesRepository userPreferencesRepository) {
        this.lobbyRepository = lobbyRepository;
        this.usersRepository = usersRepository;
        this.userPreferencesRepository = userPreferencesRepository;
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
    public void saveUserPreferences(int lobbyId, int userId, String streamingPlatform, String genre, String type) {
        UserPreferences preferences = userPreferencesRepository.findByLobby_IdLobby(lobbyId)
                .stream()
                .filter(p -> p.getUser().getId_user() == userId)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje w tym lobby."));

        preferences.setStreamingPlatform(streamingPlatform);
        preferences.setGenre(genre);
        preferences.setType(type);
        preferences.setReady(true);
        userPreferencesRepository.save(preferences);

        boolean allReady = !userPreferencesRepository.existsByLobby_IdLobbyAndIsReadyFalse(lobbyId);
        if (allReady) {
            Lobby lobby = preferences.getLobby();
            lobby.setReady(true);
            lobbyRepository.save(lobby);
        }
    }

    @Override
    public boolean areAllUsersReady(int lobbyId) {
        return !userPreferencesRepository.existsByLobby_IdLobbyAndIsReadyFalse(lobbyId);
    }
}
