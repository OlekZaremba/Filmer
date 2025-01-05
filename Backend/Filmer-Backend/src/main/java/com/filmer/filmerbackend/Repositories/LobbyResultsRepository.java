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

/**
 * Repozytorium dla operacji na encji {@link LobbyResults}.
 * Umożliwia zarządzanie wynikami głosowania w lobby.
 */
@Repository
public interface LobbyResultsRepository extends JpaRepository<LobbyResults, Integer> {

    /**
     * Zlicza głosy dla każdego filmu w danym lobby i sortuje wyniki według liczby głosów w kolejności malejącej.
     *
     * @param lobbyId identyfikator lobby
     * @return lista wyników głosowania w formacie: [Film, liczba głosów]
     */
    @Query("SELECT lr.film, COUNT(lr) AS voteCount " +
            "FROM LobbyResults lr " +
            "WHERE lr.lobby.idLobby = :lobbyId " +
            "GROUP BY lr.film " +
            "ORDER BY voteCount DESC")
    List<Object[]> countVotesByLobby(Integer lobbyId);

    /**
     * Zlicza liczbę głosów oddanych przez użytkownika w danym lobby.
     *
     * @param lobby obiekt lobby
     * @param user  obiekt użytkownika
     * @return liczba głosów oddanych przez użytkownika w lobby
     */
    @Query("SELECT COUNT(lr) FROM LobbyResults lr WHERE lr.lobby = :lobby AND lr.user = :user")
    long countByLobbyAndUser(@Param("lobby") Lobby lobby, @Param("user") Users user);

    /**
     * Sprawdza, czy istnieje wynik głosowania dla danego lobby, filmu i użytkownika.
     *
     * @param lobby obiekt lobby
     * @param film  obiekt filmu
     * @param user  obiekt użytkownika
     * @return true, jeśli wynik istnieje; false w przeciwnym razie
     */
    boolean existsByLobbyAndFilmAndUser(Lobby lobby, Films film, Users user);

    /**
     * Zlicza wszystkie głosy oddane w danym lobby.
     *
     * @param lobby obiekt lobby
     * @return liczba głosów w lobby
     */
    long countByLobby(Lobby lobby);
}
