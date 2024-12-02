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

@Entity
@Table(name = "film_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film_type")
    private Integer idFilmType;

    @Column(name = "film_type", nullable = false, length = 45)
    private String filmType;
}
