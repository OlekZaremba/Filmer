package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Films;

import java.util.Map;
import java.util.List;

/**
 * Interfejs serwisu do zarządzania wynikami głosowania w lobby.
 */
public interface ResultsService {

    /**
     * Pobiera wyniki głosowania na podstawie kodu lobby.
     *
     * @param lobbyCode kod lobby
     * @return mapa wyników, gdzie klucz to miejsce (np. 1, 2, 3), a wartość to lista filmów na danym miejscu
     */
    Map<Integer, List<Films>> getResultsByLobbyCode(String lobbyCode);

    /**
     * Wysyła wyniki głosowania na e-mail.
     *
     * @param lobbyCode kod lobby
     * @param email     adres e-mail odbiorcy
     */
    void sendResultsEmail(String lobbyCode, String email);
}

