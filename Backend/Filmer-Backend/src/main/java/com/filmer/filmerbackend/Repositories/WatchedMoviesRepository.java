package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.WatchedMovies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WatchedMoviesRepository extends JpaRepository<WatchedMovies, Integer> {

    @Query("SELECT wm FROM WatchedMovies wm WHERE wm.film.idFilm = :filmId AND wm.user.id_user = :userId")
    Optional<WatchedMovies> findByFilmIdAndUserId(@Param("filmId") Integer filmId, @Param("userId") Integer userId);
}
