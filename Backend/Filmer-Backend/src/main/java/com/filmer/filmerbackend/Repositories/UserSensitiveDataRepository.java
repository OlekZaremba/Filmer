package com.filmer.filmerbackend.Repositories;

import com.filmer.filmerbackend.Entities.UserSensitiveData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repozytorium dla operacji na encji {@link UserSensitiveData}.
 * Umożliwia zarządzanie wrażliwymi danymi użytkowników, takimi jak adres e-mail i hasło.
 */
public interface UserSensitiveDataRepository extends JpaRepository<UserSensitiveData, Integer> {

    /**
     * Wyszukuje dane wrażliwe użytkownika na podstawie jego adresu e-mail.
     *
     * @param email adres e-mail użytkownika
     * @return obiekt {@link Optional} zawierający dane wrażliwe użytkownika lub pusty, jeśli nie znaleziono
     */
    Optional<UserSensitiveData> findByEmail(String email);
}
