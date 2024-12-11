package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "lobby")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby")
    private Integer idLobby;

    @ManyToOne
    @JoinColumn(name = "users_id_user", nullable = false)
    private Users owner;

    @Column(name = "lobby_creation_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "lobby_code", nullable = false, unique = true)
    private String lobbyCode;

    @Column(name = "is_ready", nullable = false)
    private boolean isReady;
}
