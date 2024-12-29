package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "watched_movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WatchedMovies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_watched_movies")
    private Integer idWatchedMovies;

    @ManyToOne
    @JoinColumn(name = "films_id_film", nullable = false)
    private Films film;

    @ManyToOne
    @JoinColumn(name = "users_id_user", nullable = false)
    private Users user;

    @Column(name = "rating", nullable = true)
    private Integer rating;
}
