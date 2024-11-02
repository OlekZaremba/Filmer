package com.filmer.filmerbackend.Dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private int id_user;
    private String nick;
    private String email;
}
