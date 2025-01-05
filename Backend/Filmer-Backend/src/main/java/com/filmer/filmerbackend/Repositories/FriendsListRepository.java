package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.FriendsList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repozytorium dla operacji na encji {@link FriendsList}.
 * Umożliwia zarządzanie relacjami znajomości między użytkownikami.
 */
@Repository
public interface FriendsListRepository extends JpaRepository<FriendsList, Integer> {

    /**
     * Sprawdza, czy istnieje relacja znajomości między dwoma użytkownikami.
     *
     * @param user1 identyfikator pierwszego użytkownika
     * @param user2 identyfikator drugiego użytkownika
     * @return true, jeśli relacja istnieje; false w przeciwnym razie
     */
    boolean existsByUser1AndUser2(int user1, int user2);
}
