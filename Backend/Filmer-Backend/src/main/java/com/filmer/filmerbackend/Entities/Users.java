package com.filmer.filmerbackend.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private int id_user;

    @Column(nullable = false, length = 45)
    private String nick;

    @Lob // Adnotacja wskazująca na dużą wartość binarną lub tekstową
    private byte[] profilePicture;

    @OneToOne(mappedBy = "user")
    @JsonIgnore
    private UserSensitiveData userSensitiveData;
}
