package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.MovieSources;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link MovieSources}.
 * Umożliwia zarządzanie źródłami filmów, takimi jak platformy streamingowe.
 */
@Repository
public interface MovieSourcesRepository extends JpaRepository<MovieSources, Integer> {

    /**
     * Wyszukuje źródło filmu na podstawie jego nazwy.
     *
     * @param sourceName nazwa źródła filmu
     * @return obiekt {@link Optional} zawierający znalezione źródło lub pusty, jeśli nie znaleziono
     */
    Optional<MovieSources> findBySourceName(String sourceName);
}

