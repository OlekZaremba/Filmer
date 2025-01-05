package com.filmer.filmerbackend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca użytkownika systemu.
 */
@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users {

    /**
     * Identyfikator użytkownika.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id_user;

    /**
     * Pseudonim użytkownika.
     */
    @Column(nullable = false, length = 45)
    private String nick;

    /**
     * Zdjęcie profilowe użytkownika.
     */
    @Lob
    private byte[] profilePicture;

    /**
     * Dane wrażliwe użytkownika (np. hasło, adres e-mail).
     */
    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private UserSensitiveData userSensitiveData;
}
