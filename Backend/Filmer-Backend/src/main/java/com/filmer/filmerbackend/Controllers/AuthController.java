package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Dtos.UserDTO;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Requests.LoginRequest;
import com.filmer.filmerbackend.Requests.RegistrationRequest;
import com.filmer.filmerbackend.Security.JwtUtil;
import com.filmer.filmerbackend.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsersService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(UsersService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrationRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords do not match");
        }
        String result = userService.registerUser(request.getUsername(), request.getEmail(), request.getPassword());
        if (result.equals("User already exists")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest loginRequest) {
        Map<String, String> response = new HashMap<>();
        boolean isAuthenticated = userService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        if (isAuthenticated) {
            String token = jwtUtil.generateToken(loginRequest.getEmail());
            Optional<Users> user = userService.findUserByEmail(loginRequest.getEmail());
            user.ifPresent(u -> {
                response.put("token", token);
                response.put("nick", u.getNick());
                response.put("email", u.getUserSensitiveData().getEmail());
                response.put("userId", String.valueOf(u.getId_user()));
                response.put("message", "Login successful!");
            });
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else {
            response.put("message", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @GetMapping("/details")
    public ResponseEntity<UserDTO> getUserDetails(@RequestParam String email) {
        Optional<Users> user = userService.findUserByEmail(email);
        return user.map(u -> {
            String userEmail = u.getUserSensitiveData().getEmail();
            return ResponseEntity.ok(new UserDTO(u.getId_user(), u.getNick(), userEmail));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

}
