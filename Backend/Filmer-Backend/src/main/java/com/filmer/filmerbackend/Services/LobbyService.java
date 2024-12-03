package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.Users;

import java.util.List;

public interface LobbyService {
    Lobby createLobby(int ownerId);
    void closeLobby(int lobbyId);
    void addUserToLobby(String lobbyCode, int userId);
    List<Users> getParticipants(String lobbyCode);
}
