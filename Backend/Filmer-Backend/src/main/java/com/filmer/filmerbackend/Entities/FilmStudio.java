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
 * Encja reprezentująca studio filmowe.
 */
@Entity
@Table(name = "film_studio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmStudio {

    /**
     * Identyfikator studia filmowego.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film_studio")
    private Integer idFilmStudio;

    /**
     * Nazwa studia filmowego.
     */
    @Column(name = "studio_name", nullable = false, length = 45)
    private String studioName;
}
