package com.filmer.filmerbackend.Requests;

import lombok.Getter;
import lombok.Setter;

/**
 * Klasa reprezentująca żądanie logowania użytkownika.
 * Zawiera dane niezbędne do uwierzytelnienia, takie jak adres e-mail i hasło.
 */
@Getter
@Setter
public class LoginRequest {

    /**
     * Adres e-mail użytkownika.
     */
    private String email;

    /**
     * Hasło użytkownika.
     */
    private String password;
}

