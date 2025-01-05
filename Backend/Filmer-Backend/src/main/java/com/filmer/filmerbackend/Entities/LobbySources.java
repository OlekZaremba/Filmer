package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca powiązanie lobby z dostępnymi źródłami filmów.
 */
@Entity
@Table(name = "lobby_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbySources {

    /**
     * Identyfikator powiązania lobby ze źródłem.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby_source")
    private Integer idLobbySource;

    /**
     * Lobby, do którego przypisane jest źródło.
     */
    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    /**
     * Źródło filmów przypisane do lobby.
     */
    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private MovieSources source;
}
