package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.FilmGenres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FilmGenresRepository extends JpaRepository<FilmGenres, Integer> {
    Optional<FilmGenres> findByGenreName(String genreName);
}

