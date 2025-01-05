package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca powiązanie pomiędzy lobby a filmami.
 */
@Entity
@Table(name = "lobby_has_films")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyHasFilms {

    /**
     * Identyfikator powiązania lobby z filmem.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby_has_films")
    private Integer idLobbyHasFilms;

    /**
     * Lobby, w którym film jest dostępny.
     */
    @ManyToOne
    @JoinColumn(name = "lobby_id_lobby", nullable = false)
    private Lobby lobby;

    /**
     * Film dostępny w lobby.
     */
    @ManyToOne
    @JoinColumn(name = "films_id_film", nullable = false)
    private Films film;
}
