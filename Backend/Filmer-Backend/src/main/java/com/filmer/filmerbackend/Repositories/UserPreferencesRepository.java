package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.UserPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Integer> {
    List<UserPreferences> findByLobby_IdLobby(Integer lobbyId);

    boolean existsByLobby_IdLobbyAndIsReadyFalse(Integer lobbyId);
}
