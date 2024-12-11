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

    @ManyToOne(optional = false)
    @JoinColumn(name = "lobby_id", foreignKey = @ForeignKey(name = "fk_user_preferences_lobby"))
    private Lobby lobby;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_preferences_user"))
    private Users user;

    @ManyToOne
    @JoinColumn(name = "streaming_platform_id", foreignKey = @ForeignKey(name = "fk_user_preferences_platform"))
    private MovieSources streamingPlatform;

    @ManyToOne
    @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "fk_user_preferences_genre"))
    private FilmGenres genre;

    @ManyToOne
    @JoinColumn(name = "type_id", foreignKey = @ForeignKey(name = "fk_user_preferences_type"))
    private FilmType type;

    @Column(name = "is_ready", nullable = false)
    private boolean isReady;
}
