package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Films {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film")
    private Integer idFilm;

    @Column(name = "film_name", nullable = false, length = 45)
    private String filmName;

    @Lob
    @Column(name = "film_image")
    private byte[] filmImage;

    @Column(name = "film_desc", nullable = false, columnDefinition = "TEXT")
    private String filmDesc;

    @ManyToOne
    @JoinColumn(name = "film_director_id", nullable = false)
    private FilmDirector director;

    @ManyToOne
    @JoinColumn(name = "film_studio_id", nullable = false)
    private FilmStudio studio;

    @ManyToOne
    @JoinColumn(name = "film_type_id", nullable = false)
    private FilmType type;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private MovieSources source;

    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private FilmGenres genre;
}
