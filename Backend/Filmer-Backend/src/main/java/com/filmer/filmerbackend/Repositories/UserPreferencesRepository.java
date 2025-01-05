package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repozytorium dla operacji na encji {@link UserPreferences}.
 * Umożliwia zarządzanie preferencjami użytkowników w kontekście lobby.
 */
@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Integer> {

    /**
     * Wyszukuje wszystkie preferencje użytkowników przypisane do danego lobby.
     *
     * @param lobbyId identyfikator lobby
     * @return lista preferencji użytkowników w danym lobby
     */
    List<UserPreferences> findByLobby_IdLobby(Integer lobbyId);

    /**
     * Sprawdza, czy w danym lobby istnieją użytkownicy, którzy nie są gotowi.
     *
     * @param lobbyId identyfikator lobby
     * @return true, jeśli istnieją użytkownicy niegotowi; false w przeciwnym razie
     */
    boolean existsByLobby_IdLobbyAndIsReadyFalse(Integer lobbyId);
}
