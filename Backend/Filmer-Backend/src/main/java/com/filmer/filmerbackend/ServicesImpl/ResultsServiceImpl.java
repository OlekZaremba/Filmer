package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Repositories.LobbyRepository;
import com.filmer.filmerbackend.Repositories.LobbyResultsRepository;
import com.filmer.filmerbackend.Services.ResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultsServiceImpl implements ResultsService {

    private final LobbyRepository lobbyRepository;
    private final LobbyResultsRepository lobbyResultsRepository;

    @Override
    public Map<Integer, List<Films>> getResultsByLobbyCode(String lobbyCode) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        List<Object[]> rawResults = lobbyResultsRepository.countVotesByLobby(lobby.getIdLobby());

        Map<Integer, List<Films>> results = rawResults.stream()
                .collect(Collectors.groupingBy(
                        row -> ((Number) row[1]).intValue(),
                        TreeMap::new,
                        Collectors.mapping(row -> (Films) row[0], Collectors.toList())
                ));

        Map<Integer, List<Films>> sortedResults = new TreeMap<>(Collections.reverseOrder());
        sortedResults.putAll(results);

        return sortedResults;
    }
}
