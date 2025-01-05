package com.filmer.filmerbackend.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

/**
 * Encja reprezentująca reżysera filmu.
 */
@Entity
@Table(name = "film_director")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmDirector {

    /**
     * Identyfikator reżysera filmu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film_director")
    private Integer idFilmDirector;

    /**
     * Imię i nazwisko reżysera.
     */
    @Column(name = "name", nullable = false, length = 45)
    private String name;
}
