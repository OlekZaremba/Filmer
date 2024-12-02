package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.LobbyUsers;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.LobbyRepository;
import com.filmer.filmerbackend.Repositories.LobbyUsersRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import com.filmer.filmerbackend.Services.LobbyService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LobbyServiceImpl implements LobbyService {

    private final LobbyRepository lobbyRepository;
    private final UsersRepository usersRepository;
    private final LobbyUsersRepository lobbyUsersRepository;

    public LobbyServiceImpl(LobbyRepository lobbyRepository, UsersRepository usersRepository, LobbyUsersRepository lobbyUsersRepository) {
        this.lobbyRepository = lobbyRepository;
        this.usersRepository = usersRepository;
        this.lobbyUsersRepository = lobbyUsersRepository;
    }

    @Override
    public Lobby createLobby(int ownerId) {
        Users owner = usersRepository.findById(ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        Lobby lobby = new Lobby();
        lobby.setOwner(owner);
        lobby.setCreationDate(new Date());
        lobby.setActive(true);

        return lobbyRepository.save(lobby);
    }

    @Override
    public void closeLobby(int lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        lobby.setActive(false);
        lobbyRepository.save(lobby);
    }

    @Override
    public void addUserToLobby(int lobbyId, int userId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        boolean alreadyInLobby = lobbyUsersRepository.findByLobbyAndUser(lobbyId, userId).isPresent();
        if (alreadyInLobby) {
            throw new IllegalArgumentException("Użytkownik jest już w lobby.");
        }

        LobbyUsers lobbyUser = new LobbyUsers();
        lobbyUser.setLobby(lobby);
        lobbyUser.setUser(user);

        lobbyUsersRepository.save(lobbyUser);
    }
}