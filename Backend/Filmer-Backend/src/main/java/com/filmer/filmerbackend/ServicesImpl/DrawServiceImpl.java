package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.*;
import com.filmer.filmerbackend.Repositories.*;
import com.filmer.filmerbackend.Services.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DrawServiceImpl implements DrawService {

    private final FilmsRepository filmsRepository;
    private final LobbyRepository lobbyRepository;
    private final LobbyHasFilmsRepository lobbyHasFilmsRepository;
    private final LobbyResultsRepository lobbyResultsRepository;
    private final UsersRepository usersRepository;

    @Override
    public List<Films> drawFilms(String lobbyCode) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        List<LobbyHasFilms> existingLobbyFilms = lobbyHasFilmsRepository.findByLobby(lobby);
        if (!existingLobbyFilms.isEmpty()) {
            return existingLobbyFilms.stream().map(LobbyHasFilms::getFilm).collect(Collectors.toList());
        }

        List<Films> selectedFilms = lobby.getUserPreferences().stream()
                .flatMap(pref -> filmsRepository.findFilmsByPreferences(
                        pref.getGenre() != null ? pref.getGenre().getIdGenre() : null,
                        pref.getType() != null ? pref.getType().getIdFilmType() : null,
                        pref.getStreamingPlatform() != null ? pref.getStreamingPlatform().getIdMovieSource() : null
                ).stream())
                .distinct()
                .limit(lobby.getUserPreferences().size())
                .collect(Collectors.toList());

        List<Films> randomFilms = filmsRepository.findRandomFilmsExcluding(
                selectedFilms.stream().map(Films::getIdFilm).collect(Collectors.toList()),
                16 - selectedFilms.size()
        );

        selectedFilms.addAll(randomFilms);

        selectedFilms.forEach(film -> {
            LobbyHasFilms lobbyFilm = new LobbyHasFilms();
            lobbyFilm.setLobby(lobby);
            lobbyFilm.setFilm(film);
            lobbyHasFilmsRepository.save(lobbyFilm);
        });

        return selectedFilms;
    }

    @Override
    public void submitVote(String lobbyCode, Integer filmId, Integer userId) {
        Lobby lobby = lobbyRepository.findByLobbyCode(lobbyCode)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        Films film = filmsRepository.findById(filmId)
                .orElseThrow(() -> new IllegalArgumentException("Film nie istnieje."));

        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Użytkownik nie istnieje."));

        if (filmId != null) {
            LobbyResults result = new LobbyResults();
            result.setLobby(lobby);
            result.setFilm(film);
            result.setUser(user);
            lobbyResultsRepository.save(result);
        }

        long votesByUser = lobbyResultsRepository.countByLobbyAndUser(lobby, user);
        long totalFilms = 16;

        if (votesByUser >= totalFilms) {
            lobby.setFinishedPlayersCount(lobby.getFinishedPlayersCount() + 1);
            lobbyRepository.save(lobby);

            System.out.println("Gracz zakończył głosowanie: User=" + user.getId_user() +
                    ", FinishedPlayersCount=" + lobby.getFinishedPlayersCount());

            long totalPlayers = lobby.getUserPreferences().size();
            if (lobby.getFinishedPlayersCount() >= totalPlayers) {
                lobby.setVotingCompleted(true);
                lobbyRepository.save(lobby);
                System.out.println("Głosowanie zakończone! Flaga ustawiona na true.");
            }
        }
    }

}
