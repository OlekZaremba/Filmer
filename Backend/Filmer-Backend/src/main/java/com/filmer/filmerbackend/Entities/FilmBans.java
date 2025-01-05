package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

/**
 * Encja reprezentująca informacje o zbanowanych filmach w lobby.
 */
@Entity
@Table(name = "film_bans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmBans {

    /**
     * Identyfikator zakazu filmu.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film_ban")
    private Integer idFilmBan;

    /**
     * Lobby, w którym film został zbanowany.
     */
    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    /**
     * Film, który został zbanowany.
     */
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Films film;

    /**
     * Użytkownik, który zbanował film.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    /**
     * Czas, w którym film został zbanowany.
     */
    @Column(name = "ban_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date banTime;
}
