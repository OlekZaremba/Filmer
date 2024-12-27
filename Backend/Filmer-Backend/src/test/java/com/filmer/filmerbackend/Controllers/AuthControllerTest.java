package com.filmer.filmerbackend.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filmer.filmerbackend.Entities.UserSensitiveData;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Requests.LoginRequest;
import com.filmer.filmerbackend.Requests.RegistrationRequest;
import com.filmer.filmerbackend.Security.JwtUtil;
import com.filmer.filmerbackend.Services.UsersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UsersService usersService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void register_ShouldReturnCreated_WhenRegistrationIsSuccessful() throws Exception {
        RegistrationRequest request = new RegistrationRequest("testUser", "test@example.com", "password123", "password123");

        when(usersService.registerUser(request.getUsername(), request.getEmail(), request.getPassword()))
                .thenReturn("User registered successfully");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void register_ShouldReturnBadRequest_WhenPasswordsDoNotMatch() throws Exception {
        RegistrationRequest request = new RegistrationRequest("testUser", "test@example.com", "password123", "password456");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Passwords do not match"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void register_ShouldReturnConflict_WhenUserAlreadyExists() throws Exception {
        RegistrationRequest request = new RegistrationRequest("testUser", "test@example.com", "password123", "password123");

        when(usersService.registerUser(request.getUsername(), request.getEmail(), request.getPassword()))
                .thenReturn("User already exists");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isConflict())
                .andExpect(content().string("User already exists"));
    }

//    @Test
//    @WithMockUser(username = "testUser", roles = {"USER"})
//    void login_ShouldReturnOk_WhenLoginIsSuccessful() throws Exception {
//        LoginRequest request = new LoginRequest("test@example.com", "password123");
//        String token = "jwt-token";
//
//        Users user = new Users();
//        user.setId_user(1);
//        user.setNick("testUser");
//
//        UserSensitiveData sensitiveData = new UserSensitiveData();
//        sensitiveData.setEmail("test@example.com");
//        user.setUserSensitiveData(sensitiveData);
//
//        when(usersService.authenticateUser(request.getEmail(), request.getPassword())).thenReturn(true);
//        when(jwtUtil.generateToken(request.getEmail())).thenReturn(token);
//        when(usersService.findUserByEmail(request.getEmail())).thenReturn(Optional.of(user));
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .with(csrf()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").value(token))
//                .andExpect(jsonPath("$.nick").value("testUser"))
//                .andExpect(jsonPath("$.email").value("test@example.com"))
//                .andExpect(jsonPath("$.userId").value("1"))
//                .andExpect(jsonPath("$.message").value("Login successful!"));
//    }

//    @Test
//    @WithMockUser(username = "testUser", roles = {"USER"})
//    void login_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() throws Exception {
//        LoginRequest request = new LoginRequest("test@example.com", "wrongPassword");
//
//        when(usersService.authenticateUser(request.getEmail(), request.getPassword())).thenReturn(false);
//
//        mockMvc.perform(post("/api/auth/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request))
//                        .with(csrf()))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.message").value("Invalid email or password"));
//    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void getUserDetails_ShouldReturnOk_WhenUserExists() throws Exception {
        String email = "test@example.com";

        Users user = new Users();
        user.setId_user(1);
        user.setNick("testUser");

        UserSensitiveData sensitiveData = new UserSensitiveData();
        sensitiveData.setEmail(email);
        user.setUserSensitiveData(sensitiveData);

        when(usersService.findUserByEmail(email)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/auth/details")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_user").value(1))
                .andExpect(jsonPath("$.nick").value("testUser"))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void getUserDetails_ShouldReturnNotFound_WhenUserDoesNotExist() throws Exception {
        String email = "nonexistent@example.com";

        when(usersService.findUserByEmail(email)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/auth/details")
                        .param("email", email))
                .andExpect(status().isNotFound());
    }
}
