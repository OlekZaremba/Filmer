package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.Users;

import java.util.List;

/**
 * Interfejs serwisu do zarządzania lobby i grami.
 */
public interface LobbyService {

    /**
     * Tworzy nowe lobby z określonym właścicielem.
     *
     * @param ownerId identyfikator właściciela lobby
     * @return utworzone lobby
     */
    Lobby createLobby(int ownerId);

    /**
     * Zamyka lobby na podstawie jego identyfikatora.
     *
     * @param lobbyId identyfikator lobby
     */
    void closeLobby(int lobbyId);

    /**
     * Dodaje użytkownika do lobby na podstawie jego kodu.
     *
     * @param lobbyCode kod lobby
     * @param userId    identyfikator użytkownika
     */
    void addUserToLobby(String lobbyCode, int userId);

    /**
     * Pobiera listę uczestników lobby.
     *
     * @param lobbyCode kod lobby
     * @return lista uczestników lobby
     */
    List<Users> getParticipants(String lobbyCode);

    /**
     * Sprawdza, czy wszyscy użytkownicy w lobby są gotowi.
     *
     * @param lobbyId identyfikator lobby
     * @return true, jeśli wszyscy użytkownicy są gotowi, false w przeciwnym razie
     */
    boolean areAllUsersReady(int lobbyId);

    /**
     * Zapisuje preferencje użytkownika dotyczące platformy streamingowej, gatunku i typu filmu.
     *
     * @param lobbyCode kod lobby
     * @param userId    identyfikator użytkownika
     * @param streamingPlatform preferowana platforma streamingowa
     * @param genre     preferowany gatunek
     * @param type      preferowany typ filmu
     */
    void saveUserPreferences(String lobbyCode, int userId, String streamingPlatform, String genre, String type);

    /**
     * Pobiera lobby na podstawie jego kodu.
     *
     * @param lobbyCode kod lobby
     * @return obiekt lobby
     */
    Lobby getLobbyByCode(String lobbyCode);

    /**
     * Rozpoczyna grę w określonym lobby.
     *
     * @param lobbyId identyfikator lobby
     */
    void startGame(int lobbyId);

    /**
     * Sprawdza, czy głosowanie w lobby zostało zakończone.
     *
     * @param lobbyCode kod lobby
     * @return true, jeśli głosowanie zostało zakończone, false w przeciwnym razie
     */
    boolean checkVotingCompletion(String lobbyCode);

    /**
     * Kończy głosowanie dla użytkownika w lobby.
     *
     * @param lobbyCode kod lobby
     * @param userId    identyfikator użytkownika
     */
    void finishVoting(String lobbyCode, int userId);
}

