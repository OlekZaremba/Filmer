package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Users;

import java.util.Optional;

public interface UsersService {
    Optional<Users> findUserByNick(String nick);
    Optional<Users> findUserByEmail(String email);
    boolean authenticateUser(String email, String password);
}
