package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link Users}.
 * Umożliwia zarządzanie użytkownikami systemu, w tym wyszukiwanie według pseudonimu i relacji znajomości.
 */
@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    /**
     * Wyszukuje użytkownika na podstawie jego pseudonimu.
     *
     * @param nick pseudonim użytkownika
     * @return obiekt {@link Optional} zawierający znalezionego użytkownika lub pusty, jeśli nie znaleziono
     */
    Optional<Users> findByNick(String nick);

    /**
     * Wyszukuje listę znajomych użytkownika na podstawie jego identyfikatora.
     *
     * @param userId identyfikator użytkownika
     * @return lista znajomych użytkownika
     */
    @Query("SELECT u FROM users u JOIN friends_list f ON u.id_user = f.user2 WHERE f.user1 = :userId")
    List<Users> findFriendsByUserId(@Param("userId") int userId);

    /**
     * Wyszukuje użytkowników, których pseudonim częściowo pasuje do podanego tekstu.
     *
     * @param nick fragment pseudonimu użytkownika
     * @return lista użytkowników pasujących do wzorca wyszukiwania
     */
    @Query("SELECT u FROM users u WHERE LOWER(u.nick) LIKE LOWER(CONCAT('%', :nick, '%'))")
    List<Users> findByPartialNick(@Param("nick") String nick);
}
