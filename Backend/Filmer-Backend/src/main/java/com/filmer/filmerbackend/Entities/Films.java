package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca film w systemie.
 */
@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Films {

    /**
     * Identyfikator filmu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film")
    private Integer idFilm;

    /**
     * Nazwa filmu.
     */
    @Column(name = "film_name", nullable = false, length = 45)
    private String filmName;

    /**
     * Obraz reprezentujący film (np. okładka).
     */
    @Lob
    @Column(name = "film_image")
    private byte[] filmImage;

    /**
     * Opis filmu.
     */
    @Column(name = "film_desc", nullable = false, columnDefinition = "TEXT")
    private String filmDesc;

    /**
     * Reżyser filmu.
     */
    @ManyToOne
    @JoinColumn(name = "film_director_id", nullable = false)
    private FilmDirector director;

    /**
     * Studio filmowe odpowiedzialne za produkcję filmu.
     */
    @ManyToOne
    @JoinColumn(name = "film_studio_id", nullable = false)
    private FilmStudio studio;

    /**
     * Typ filmu (np. pełnometrażowy, krótkometrażowy).
     */
    @ManyToOne
    @JoinColumn(name = "film_type_id", nullable = false)
    private FilmType type;

    /**
     * Źródło filmu (np. platforma streamingowa).
     */
    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private MovieSources source;

    /**
     * Gatunek filmu.
     */
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private FilmGenres genre;
}
