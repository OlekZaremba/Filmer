package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.LobbyUsers;
import com.filmer.filmerbackend.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link LobbyUsers}.
 * Umożliwia zarządzanie powiązaniami użytkowników z lobby.
 */
@Repository
public interface LobbyUsersRepository extends JpaRepository<LobbyUsers, Integer> {

    /**
     * Wyszukuje powiązanie użytkownika z danym lobby na podstawie ich identyfikatorów.
     *
     * @param lobbyId identyfikator lobby
     * @param userId  identyfikator użytkownika
     * @return obiekt {@link Optional} zawierający powiązanie użytkownika z lobby lub pusty, jeśli nie znaleziono
     */
    @Query("SELECT lu FROM LobbyUsers lu WHERE lu.lobby.idLobby = :lobbyId AND lu.user.id_user = :userId")
    Optional<LobbyUsers> findByLobbyAndUser(@Param("lobbyId") int lobbyId, @Param("userId") int userId);

    /**
     * Wyszukuje wszystkich użytkowników przypisanych do danego lobby.
     *
     * @param lobbyId identyfikator lobby
     * @return lista użytkowników przypisanych do lobby
     */
    @Query("SELECT lu.user FROM LobbyUsers lu WHERE lu.lobby.idLobby = :lobbyId")
    List<Users> findUsersByLobbyId(@Param("lobbyId") int lobbyId);
}
