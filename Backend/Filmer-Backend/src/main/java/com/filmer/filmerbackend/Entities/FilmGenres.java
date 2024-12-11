package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "film_genres")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmGenres {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_genre")
    private Integer idGenre;

    @Column(name = "genre_name", nullable = false, unique = true)
    private String genreName;
}
