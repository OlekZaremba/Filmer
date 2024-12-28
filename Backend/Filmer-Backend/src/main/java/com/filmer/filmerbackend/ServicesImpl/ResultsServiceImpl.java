package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Repositories.LobbyRepository;
import com.filmer.filmerbackend.Repositories.LobbyResultsRepository;
import com.filmer.filmerbackend.Services.ResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultsServiceImpl implements ResultsService {

    private final LobbyRepository lobbyRepository;
    private final LobbyResultsRepository lobbyResultsRepository;
    private final JavaMailSender mailSender;

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

        Map<Integer, List<Films>> sortedResults = new TreeMap<>();
        sortedResults.putAll(results);

        return sortedResults;
    }

    @Override
    public void sendResultsEmail(String lobbyCode, String email) {
        Map<Integer, List<Films>> results = getResultsByLobbyCode(lobbyCode);

        String formattedResults = formatResults(results);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Wyniki głosowania w lobby: " + lobbyCode);
            message.setText(formattedResults);
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Błąd podczas wysyłania e-maila: " + e.getMessage(), e);
        }
    }

    private String formatResults(Map<Integer, List<Films>> results) {
        StringBuilder sb = new StringBuilder("Wyniki głosowania:\n\n");
        results.forEach((place, films) -> {
            sb.append("Miejsce ").append(place).append(":\n");
            films.forEach(film -> sb.append(" - ").append(film.getFilmName()).append("\n"));
        });
        return sb.toString();
    }

}
