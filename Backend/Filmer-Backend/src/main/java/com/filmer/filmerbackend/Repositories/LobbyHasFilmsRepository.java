package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.LobbyHasFilms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LobbyHasFilmsRepository extends JpaRepository<LobbyHasFilms, Integer> {

    @Query("SELECT lhf FROM LobbyHasFilms lhf WHERE lhf.lobby = :lobby")
    List<LobbyHasFilms> findByLobby(@Param("lobby") Lobby lobby);
}
