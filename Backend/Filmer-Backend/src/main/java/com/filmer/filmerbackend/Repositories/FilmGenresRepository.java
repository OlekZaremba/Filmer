package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.FilmGenres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link FilmGenres}.
 * Umożliwia wyszukiwanie gatunków filmowych według nazwy.
 */
@Repository
public interface FilmGenresRepository extends JpaRepository<FilmGenres, Integer> {

    /**
     * Wyszukuje gatunek filmowy na podstawie nazwy.
     *
     * @param genreName nazwa gatunku filmu
     * @return obiekt {@link Optional} zawierający znaleziony gatunek lub pusty, jeśli nie znaleziono
     */
    Optional<FilmGenres> findByGenreName(String genreName);
}

