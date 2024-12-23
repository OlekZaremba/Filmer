package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LobbyRepository extends JpaRepository<Lobby, Integer> {
    @Query("SELECT l FROM Lobby l WHERE l.lobbyCode = :lobbyCode AND l.isActive = true")
    Optional<Lobby> findByLobbyCode(@Param("lobbyCode") String lobbyCode);
}
