package com.filmer.filmerbackend.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Klasa reprezentująca żądanie rejestracji użytkownika.
 * Zawiera dane niezbędne do utworzenia nowego konta użytkownika.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationRequest {

    /**
     * Pseudonim użytkownika.
     */
    private String username;

    /**
     * Adres e-mail użytkownika.
     */
    private String email;

    /**
     * Hasło użytkownika.
     */
    private String password;

    /**
     * Powtórzone hasło użytkownika dla potwierdzenia.
     */
    private String confirmPassword;
}
