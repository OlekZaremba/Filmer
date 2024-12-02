package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lobby_has_films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyHasFilms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby_has_films")
    private Integer idLobbyHasFilms;

    @ManyToOne
    @JoinColumn(name = "lobby_id_lobby", nullable = false)
    private Lobby lobby;

    @ManyToOne
    @JoinColumn(name = "films_id_film", nullable = false)
    private Films film;
}
