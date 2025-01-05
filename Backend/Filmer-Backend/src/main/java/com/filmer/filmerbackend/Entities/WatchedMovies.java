package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca filmy obejrzane przez użytkownika, wraz z ewentualną oceną.
 */
@Entity
@Table(name = "watched_movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WatchedMovies {

    /**
     * Identyfikator obejrzanego filmu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_watched_movies")
    private Integer idWatchedMovies;

    /**
     * Film obejrzany przez użytkownika.
     */
    @ManyToOne
    @JoinColumn(name = "films_id_film", nullable = false)
    private Films film;

    /**
     * Użytkownik, który obejrzał film.
     */
    @ManyToOne
    @JoinColumn(name = "users_id_user", nullable = false)
    private Users user;

    /**
     * Ocena filmu wystawiona przez użytkownika (opcjonalna).
     */
    @Column(name = "rating", nullable = true)
    private Integer rating;
}
