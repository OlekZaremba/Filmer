package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.WatchedMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link WatchedMovies}.
 * Umożliwia zarządzanie obejrzanymi filmami i ich ocenami przez użytkowników.
 */
@Repository
public interface WatchedMoviesRepository extends JpaRepository<WatchedMovies, Integer> {

    /**
     * Wyszukuje obejrzany film dla danego użytkownika na podstawie identyfikatora filmu i użytkownika.
     *
     * @param filmId identyfikator filmu
     * @param userId identyfikator użytkownika
     * @return obiekt {@link Optional} zawierający obejrzany film z przypisaną oceną lub pusty, jeśli nie znaleziono
     */
    @Query("SELECT wm FROM WatchedMovies wm WHERE wm.film.idFilm = :filmId AND wm.user.id_user = :userId")
    Optional<WatchedMovies> findByFilmIdAndUserId(@Param("filmId") Integer filmId, @Param("userId") Integer userId);
}
