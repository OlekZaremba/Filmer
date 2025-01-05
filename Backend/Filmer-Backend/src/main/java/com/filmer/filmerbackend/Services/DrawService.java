package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Films;

import java.util.List;

/**
 * Interfejs serwisu do zarządzania losowaniem filmów i głosowaniem w lobby.
 */
public interface DrawService {

    /**
     * Losuje listę filmów dla określonego lobby.
     *
     * @param lobbyCode kod lobby
     * @return lista wylosowanych filmów
     */
    List<Films> drawFilms(String lobbyCode);

    /**
     * Zapisuje głos użytkownika na dany film w lobby.
     *
     * @param lobbyCode kod lobby
     * @param filmId    identyfikator filmu
     * @param userId    identyfikator użytkownika
     */
    void submitVote(String lobbyCode, Integer filmId, Integer userId);
}

