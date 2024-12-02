package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "film_bans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilmBans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film_ban")
    private Integer idFilmBan;

    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Films film;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(name = "ban_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date banTime;
}
