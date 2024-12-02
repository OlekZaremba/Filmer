package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "lobby_sources")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LobbySources {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lobby_source")
    private Integer idLobbySource;

    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false)
    private Lobby lobby;

    @ManyToOne
    @JoinColumn(name = "source_id", nullable = false)
    private MovieSources source;
}
