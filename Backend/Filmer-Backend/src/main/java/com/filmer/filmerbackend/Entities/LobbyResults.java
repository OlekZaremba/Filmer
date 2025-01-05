package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca wyniki głosowania w lobby.
 */
@Entity
@Table(name = "lobby_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyResults {

    /**
     * Identyfikator wyniku głosowania.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby_result")
    private Integer idLobbyResult;

    /**
     * Lobby, do którego należy wynik głosowania.
     */
    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    /**
     * Film, na który oddano głos.
     */
    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Films film;

    /**
     * Użytkownik, który oddał głos.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
