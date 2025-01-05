package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Lobby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link Lobby}.
 * Umożliwia zarządzanie lobby w systemie.
 */
@Repository
public interface LobbyRepository extends JpaRepository<Lobby, Integer> {

    /**
     * Wyszukuje aktywne lobby na podstawie jego kodu.
     *
     * @param lobbyCode unikalny kod lobby
     * @return obiekt {@link Optional} zawierający znalezione lobby lub pusty, jeśli nie znaleziono
     */
    @Query("SELECT l FROM Lobby l WHERE l.lobbyCode = :lobbyCode AND l.isActive = true")
    Optional<Lobby> findByLobbyCode(@Param("lobbyCode") String lobbyCode);
}
