package com.filmer.filmerbackend.Entities;

import com.filmer.filmerbackend.Entities.Lobby;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "position", nullable = false)
    private Position position;

    public enum Position {
        FIRST, SECOND, THIRD
    }
}
