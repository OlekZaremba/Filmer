package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Films;

import java.util.List;

public interface DrawService {
    List<Films> drawFilms(Integer lobbyId);
    void submitVote(Integer lobbyId, Integer filmId);
    List<Object[]> getResults(Integer lobbyId);
}
