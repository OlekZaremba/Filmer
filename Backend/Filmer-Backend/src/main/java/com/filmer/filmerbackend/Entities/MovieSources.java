package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "movie_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieSources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movie_source")
    private Integer idMovieSource;

    @Column(name = "source_name", nullable = false, length = 45, unique = true)
    private String sourceName;
}
