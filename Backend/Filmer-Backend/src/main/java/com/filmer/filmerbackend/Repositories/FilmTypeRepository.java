package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.FilmType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link FilmType}.
 * Umożliwia wyszukiwanie typów filmów według nazwy.
 */
@Repository
public interface FilmTypeRepository extends JpaRepository<FilmType, Integer> {

    /**
     * Wyszukuje typ filmu na podstawie nazwy.
     *
     * @param filmType nazwa typu filmu
     * @return obiekt {@link Optional} zawierający znaleziony typ filmu lub pusty, jeśli nie znaleziono
     */
    Optional<FilmType> findByFilmType(String filmType);
}

