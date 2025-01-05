package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.Films;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link Films}.
 * Umożliwia wykonywanie zapytań dotyczących filmów, w tym wyszukiwania według preferencji, gatunków i nazw.
 */
@Repository
public interface FilmsRepository extends JpaRepository<Films, Integer> {

    /**
     * Wyszukuje filmy na podstawie podanych preferencji (gatunek, typ filmu, źródło).
     *
     * @param genreId  identyfikator gatunku (opcjonalny)
     * @param typeId   identyfikator typu filmu (opcjonalny)
     * @param sourceId identyfikator źródła filmu (opcjonalny)
     * @return lista filmów spełniających kryteria wyszukiwania
     */
    @Query("SELECT f FROM Films f " +
            "WHERE (:genreId IS NULL OR f.genre.idGenre = :genreId) " +
            "AND (:typeId IS NULL OR f.type.idFilmType = :typeId) " +
            "AND (:sourceId IS NULL OR f.source.idMovieSource = :sourceId)")
    List<Films> findFilmsByPreferences(@Param("genreId") Integer genreId,
                                       @Param("typeId") Integer typeId,
                                       @Param("sourceId") Integer sourceId);

    /**
     * Znajduje losowe filmy z wykluczeniem określonych identyfikatorów.
     *
     * @param excludedIds lista identyfikatorów filmów do wykluczenia
     * @param limit       maksymalna liczba filmów do zwrócenia
     * @return lista losowych filmów
     */
    @Query(value = "SELECT * FROM films f WHERE f.id_film NOT IN (:excludedIds) ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Films> findRandomFilmsExcluding(@Param("excludedIds") List<Integer> excludedIds, @Param("limit") int limit);

    /**
     * Znajduje losowe filmy.
     *
     * @param limit maksymalna liczba filmów do zwrócenia
     * @return lista losowych filmów
     */
    @Query(value = "SELECT * FROM films f ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Films> findRandomFilms(@Param("limit") int limit);

    /**
     * Wyszukuje film na podstawie dokładnej nazwy.
     *
     * @param filmName nazwa filmu
     * @return obiekt {@link Optional} zawierający znaleziony film lub pusty, jeśli nie znaleziono
     */
    Optional<Films> findByFilmName(String filmName);

    /**
     * Wyszukuje filmy na podstawie gatunku.
     *
     * @param genreName nazwa gatunku (case-insensitive)
     * @return lista filmów należących do danego gatunku
     */
    @Query("SELECT f FROM Films f WHERE LOWER(f.genre.genreName) = LOWER(:genreName)")
    List<Films> findByGenre(@Param("genreName") String genreName);

    /**
     * Wyszukuje filmy, których nazwa zawiera określony tekst.
     *
     * @param name fragment nazwy filmu
     * @return lista filmów pasujących do wzorca wyszukiwania
     */
    @Query("SELECT f FROM Films f WHERE LOWER(f.filmName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Films> findByFilmNameContaining(@Param("name") String name);

}

