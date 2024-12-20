package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.LobbyResults;
import com.filmer.filmerbackend.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LobbyResultsRepository extends JpaRepository<LobbyResults, Integer> {

    @Query("SELECT lr.film.idFilm, COUNT(lr) AS voteCount " +
            "FROM LobbyResults lr WHERE lr.lobby.idLobby = :lobbyId " +
            "GROUP BY lr.film.idFilm " +
            "ORDER BY voteCount DESC")
    List<Object[]> countVotesByLobby(@Param("lobbyId") Integer lobbyId);

    @Query("SELECT COUNT(lr) FROM LobbyResults lr WHERE lr.lobby = :lobby AND lr.user = :user")
    long countByLobbyAndUser(@Param("lobby") Lobby lobby, @Param("user") Users user);


    boolean existsByLobbyAndFilmAndUser(Lobby lobby, Films film, Users user);
    long countByLobby(Lobby lobby);
}
