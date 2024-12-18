package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lobby_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbyResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby_result")
    private Integer idLobbyResult;

    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    @ManyToOne
    @JoinColumn(name = "film_id", nullable = false)
    private Films film;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

}
