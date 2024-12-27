package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Films;

import java.util.Map;
import java.util.List;

public interface ResultsService {
    Map<Integer, List<Films>> getResultsByLobbyCode(String lobbyCode);
    void sendResultsEmail(String lobbyCode, String email);

}
