package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_preference")
    private Integer idUserPreference;

    @ManyToOne
    @JoinColumn(name = "lobby_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_preferences_lobby"))
    private Lobby lobby;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_preferences_user"))
    private Users user;

    @Column(name = "streaming_platform", length = 255)
    private String streamingPlatform;

    @Column(name = "genre", length = 255)
    private String genre;

    @Column(name = "type", length = 255)
    private String type;

    @Column(name = "is_ready", nullable = false)
    private boolean isReady;
}
