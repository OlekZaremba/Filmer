package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.FilmType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmTypeRepository extends JpaRepository<FilmType, Integer> {
    Optional<FilmType> findByFilmType(String filmType);
}

