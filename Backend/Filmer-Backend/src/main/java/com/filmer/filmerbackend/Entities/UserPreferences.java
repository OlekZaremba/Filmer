package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca preferencje użytkownika w kontekście lobby, takie jak gatunek, typ filmu i platforma streamingowa.
 */
@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferences {

    /**
     * Identyfikator preferencji użytkownika.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user_preference")
    private Integer idUserPreference;

    /**
     * Lobby, do którego przypisane są preferencje użytkownika.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "lobby_id", foreignKey = @ForeignKey(name = "fk_user_preferences_lobby"))
    private Lobby lobby;

    /**
     * Użytkownik, do którego przypisane są preferencje.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_user_preferences_user"))
    private Users user;

    /**
     * Preferowana platforma streamingowa użytkownika.
     */
    @ManyToOne
    @JoinColumn(name = "streaming_platform_id", foreignKey = @ForeignKey(name = "fk_user_preferences_platform"))
    private MovieSources streamingPlatform;

    /**
     * Preferowany gatunek filmowy użytkownika.
     */
    @ManyToOne
    @JoinColumn(name = "genre_id", foreignKey = @ForeignKey(name = "fk_user_preferences_genre"))
    private FilmGenres genre;

    /**
     * Preferowany typ filmu użytkownika.
     */
    @ManyToOne
    @JoinColumn(name = "type_id", foreignKey = @ForeignKey(name = "fk_user_preferences_type"))
    private FilmType type;

    /**
     * Flaga oznaczająca, czy użytkownik jest gotowy do rozpoczęcia gry w lobby.
     */
    @Column(name = "is_ready", nullable = false)
    private boolean isReady;
}
