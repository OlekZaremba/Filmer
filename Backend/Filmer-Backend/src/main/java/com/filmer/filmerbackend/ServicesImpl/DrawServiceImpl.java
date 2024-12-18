package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.LobbyHasFilms;
import com.filmer.filmerbackend.Entities.LobbyResults;
import com.filmer.filmerbackend.Repositories.FilmsRepository;
import com.filmer.filmerbackend.Repositories.LobbyHasFilmsRepository;
import com.filmer.filmerbackend.Repositories.LobbyRepository;
import com.filmer.filmerbackend.Repositories.LobbyResultsRepository;
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

    @Override
    public List<Films> drawFilms(Integer lobbyId) {
        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));

        List<Films> selectedFilms = lobby.getUserPreferences().stream()
                .flatMap(pref -> filmsRepository.findFilmsByPreferences(
                        pref.getGenre() != null ? pref.getGenre().getIdGenre() : null,
                        pref.getType() != null ? pref.getType().getIdFilmType() : null,
                        pref.getStreamingPlatform() != null ? pref.getStreamingPlatform().getIdMovieSource() : null
                ).stream())
                .distinct()
                .limit(lobby.getUserPreferences().size())
                .collect(Collectors.toList());

        List<Integer> selectedIds = selectedFilms.stream()
                .map(Films::getIdFilm)
                .collect(Collectors.toList());

        List<Films> randomFilms = filmsRepository.findRandomFilmsExcluding(selectedIds, 16 - selectedFilms.size());
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
    public void submitVote(Integer lobbyId, Integer filmId) {
        LobbyResults result = new LobbyResults();

        Lobby lobby = lobbyRepository.findById(lobbyId)
                .orElseThrow(() -> new IllegalArgumentException("Lobby nie istnieje."));
        Films film = filmsRepository.findById(filmId)
                .orElseThrow(() -> new IllegalArgumentException("Film nie istnieje."));

        result.setLobby(lobby);
        result.setFilm(film);

        lobbyResultsRepository.save(result);
    }


    @Override
    public List<Object[]> getResults(Integer lobbyId) {
        return lobbyResultsRepository.countVotesByLobby(lobbyId);
    }
}
