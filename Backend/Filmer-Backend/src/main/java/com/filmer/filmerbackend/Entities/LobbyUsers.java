package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca powiązanie użytkowników z lobby.
 */
@Entity
@Table(name = "lobby_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyUsers {

    /**
     * Identyfikator powiązania użytkownika z lobby.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby_user")
    private Integer idLobbyUser;

    /**
     * Lobby, do którego przypisany jest użytkownik.
     */
    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    /**
     * Użytkownik przypisany do lobby.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
