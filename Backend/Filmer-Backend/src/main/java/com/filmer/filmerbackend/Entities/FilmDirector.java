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
@Table(name = "film_director")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmDirector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film_director")
    private Integer idFilmDirector;

    @Column(name = "name", nullable = false, length = 45)
    private String name;
}
