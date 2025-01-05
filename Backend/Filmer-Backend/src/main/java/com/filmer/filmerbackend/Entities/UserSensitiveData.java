package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Encja reprezentująca dane wrażliwe użytkownika, takie jak adres e-mail i hasło.
 */
@Entity(name = "user_sensitive_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSensitiveData {

    /**
     * Identyfikator danych wrażliwych użytkownika.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_user_sensitive_data;

    /**
     * Adres e-mail użytkownika.
     */
    @Column(nullable = false, length = 45)
    private String email;

    /**
     * Zaszyfrowane hasło użytkownika.
     */
    @Column(nullable = false, length = 300)
    private String password;

    /**
     * Powiązany użytkownik, którego dotyczą dane wrażliwe.
     */
    @OneToOne
    @JoinColumn(name = "users_id_user", referencedColumnName = "id_user", foreignKey = @ForeignKey(name = "FK_USER_SENSITIVE_DATA_USER"))
    private Users user;
}
