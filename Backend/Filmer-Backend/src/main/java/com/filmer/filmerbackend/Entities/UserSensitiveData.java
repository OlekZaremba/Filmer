package com.filmer.filmerbackend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user_sensitive_data")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSensitiveData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id_user_sensitive_data;

    @Column(nullable = false, length = 45)
    private String email;

    @Column(nullable = false, length = 300)
    private String password;

    @OneToOne
    @JoinColumn(name = "users_id_user", referencedColumnName = "id_user", foreignKey = @ForeignKey(name = "FK_USER_SENSITIVE_DATA_USER"))
    private Users user;
}
