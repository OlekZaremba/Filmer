package com.filmer.filmerbackend.Helpers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String confirmPassword;

}
