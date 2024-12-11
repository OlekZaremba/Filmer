package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.MovieSources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MovieSourcesRepository extends JpaRepository<MovieSources, Integer> {
    Optional<MovieSources> findBySourceName(String sourceName);
}

