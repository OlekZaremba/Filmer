package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Helpers.LoginRequest;
import com.filmer.filmerbackend.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final UsersService userService;

    @Autowired
    public LoginController(UsersService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        Optional<Users> user = userService.findUserByEmail(loginRequest.getEmail());

        if (user.isPresent() && userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword())) {
            return ResponseEntity.ok("Login successful!");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }
}
