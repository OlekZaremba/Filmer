package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Lobby;

public interface LobbyService {
    Lobby createLobby(int ownerId);
    void closeLobby(int lobbyId);
    void addUserToLobby(String lobbyCode, int userId);
}
