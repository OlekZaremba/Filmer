package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Users;

import java.util.List;
import java.util.Optional;

/**
 * Interfejs serwisu do zarządzania operacjami związanymi z użytkownikami.
 */
public interface UsersService {

    /**
     * Wyszukuje użytkownika na podstawie jego pseudonimu.
     *
     * @param nick pseudonim użytkownika
     * @return obiekt Optional zawierający użytkownika, jeśli został znaleziony, lub pusty, jeśli nie
     */
    Optional<Users> findUserByNick(String nick);

    /**
     * Wyszukuje użytkownika na podstawie jego adresu e-mail.
     *
     * @param email adres e-mail użytkownika
     * @return obiekt Optional zawierający użytkownika, jeśli został znaleziony, lub pusty, jeśli nie
     */
    Optional<Users> findUserByEmail(String email);

    /**
     * Uwierzytelnia użytkownika na podstawie jego adresu e-mail i hasła.
     *
     * @param email    adres e-mail użytkownika
     * @param password hasło użytkownika
     * @return true, jeśli uwierzytelnienie powiodło się, false w przeciwnym razie
     */
    boolean authenticateUser(String email, String password);

    /**
     * Rejestruje nowego użytkownika.
     *
     * @param username pseudonim nowego użytkownika
     * @param email    adres e-mail nowego użytkownika
     * @param password hasło nowego użytkownika
     * @return komunikat informujący o statusie rejestracji
     */
    String registerUser(String username, String email, String password);

    /**
     * Pobiera listę znajomych dla danego użytkownika.
     *
     * @param userId identyfikator użytkownika
     * @return lista znajomych powiązanych z użytkownikiem
     */
    List<Users> getFriendsByUserId(int userId);
}


