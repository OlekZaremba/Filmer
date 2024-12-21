package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Films;

import java.util.List;

public interface DrawService {
    List<Films> drawFilms(String lobbyCode);
    void submitVote(String lobbyCode, Integer filmId, Integer userId);
}
