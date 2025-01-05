package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca źródła filmów, takie jak platformy streamingowe.
 */
@Entity
@Table(name = "movie_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSources {

    /**
     * Identyfikator źródła filmu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movie_source")
    private Integer idMovieSource;

    /**
     * Nazwa źródła filmu (np. nazwa platformy streamingowej).
     */
    @Column(name = "source_name", nullable = false, length = 45, unique = true)
    private String sourceName;
}
