package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca gatunek filmu.
 */
@Entity
@Table(name = "film_genre")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmGenres {

    /**
     * Identyfikator gatunku filmu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genre")
    private Integer idGenre;

    /**
     * Nazwa gatunku filmu.
     */
    @Column(name = "genre_name", nullable = false, unique = true)
    private String genreName;
}
