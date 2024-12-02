package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lobby_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyUsers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby_user")
    private Integer idLobbyUser;

    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
}
