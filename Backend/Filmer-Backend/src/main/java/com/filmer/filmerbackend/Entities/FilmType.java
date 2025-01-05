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
 * Encja reprezentująca typ filmu.
 */
@Entity
@Table(name = "film_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmType {

    /**
     * Identyfikator typu filmu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film_type")
    private Integer idFilmType;

    /**
     * Typ filmu (np. pełnometrażowy, dokumentalny).
     */
    @Column(name = "film_type", nullable = false, length = 45)
    private String filmType;
}
