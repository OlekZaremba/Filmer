package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * Encja reprezentująca lobby, w którym użytkownicy mogą uczestniczyć w głosowaniu na filmy.
 */
@Entity
@Table(name = "lobby")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {

    /**
     * Identyfikator lobby.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby")
    private Integer idLobby;

    /**
     * Właściciel lobby.
     */
    @ManyToOne
    @JoinColumn(name = "users_id_user", nullable = false)
    private Users owner;

    /**
     * Data utworzenia lobby.
     */
    @Column(name = "lobby_creation_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    /**
     * Flaga oznaczająca, czy lobby jest aktywne.
     */
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    /**
     * Unikalny kod lobby.
     */
    @Column(name = "lobby_code", nullable = false, unique = true)
    private String lobbyCode;

    /**
     * Flaga oznaczająca, czy wszyscy użytkownicy są gotowi.
     */
    @Column(name = "is_ready", nullable = false)
    private boolean isReady;

    /**
     * Flaga oznaczająca, czy gra w lobby została rozpoczęta.
     */
    @Column(name = "is_started", nullable = false)
    private boolean isStarted = false;

    /**
     * Lista preferencji użytkowników w lobby.
     */
    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserPreferences> userPreferences;

    /**
     * Lista filmów dostępnych w lobby.
     */
    @OneToMany(mappedBy = "lobby", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LobbyHasFilms> lobbyHasFilms;

    /**
     * Flaga oznaczająca, czy głosowanie zostało zakończone.
     */
    @Column(name = "voting_completed", nullable = false)
    private Boolean votingCompleted = false;

    /**
     * Liczba użytkowników, którzy zakończyli głosowanie.
     */
    @Column(name = "finished_players_count", nullable = false)
    private Integer finishedPlayersCount = 0;
}
