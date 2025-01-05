package com.filmer.filmerbackend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO reprezentujący użytkownika w systemie.
 * Zawiera podstawowe dane użytkownika, takie jak identyfikator, pseudonim i adres e-mail.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserDTO {

    /**
     * Identyfikator użytkownika.
     */
    private int id_user;

    /**
     * Pseudonim użytkownika.
     */
    private String nick;

    /**
     * Adres e-mail użytkownika.
     */
    private String email;
}
