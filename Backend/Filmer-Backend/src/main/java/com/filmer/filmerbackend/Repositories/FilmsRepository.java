package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Films;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmsRepository extends JpaRepository<Films, Integer> {

    @Query("SELECT f FROM Films f " +
            "WHERE (:genreId IS NULL OR f.genre.idGenre = :genreId) " +
            "AND (:typeId IS NULL OR f.type.idFilmType = :typeId) " +
            "AND (:sourceId IS NULL OR f.source.idMovieSource = :sourceId)")
    List<Films> findFilmsByPreferences(@Param("genreId") Integer genreId,
                                       @Param("typeId") Integer typeId,
                                       @Param("sourceId") Integer sourceId);

    @Query(value = "SELECT * FROM films f WHERE f.id_film NOT IN (:excludedIds) ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Films> findRandomFilmsExcluding(@Param("excludedIds") List<Integer> excludedIds, @Param("limit") int limit);

    @Query(value = "SELECT * FROM films f ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Films> findRandomFilms(@Param("limit") int limit);
}
