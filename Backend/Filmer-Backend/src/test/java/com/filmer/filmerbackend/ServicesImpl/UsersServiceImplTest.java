package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.UserSensitiveData;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.UserSensitiveDataRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceImplTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private UserSensitiveDataRepository userSensitiveDataRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersServiceImpl usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserByNick() {
        String nick = "testUser";
        Users user = new Users();
        user.setNick(nick);

        when(usersRepository.findByNick(nick)).thenReturn(Optional.of(user));

        Optional<Users> result = usersService.findUserByNick(nick);

        assertTrue(result.isPresent());
        assertEquals(nick, result.get().getNick());
        verify(usersRepository, times(1)).findByNick(nick);
    }

    @Test
    void testFindUserByEmail() {
        String email = "test@example.com";
        UserSensitiveData sensitiveData = new UserSensitiveData();
        sensitiveData.setEmail(email);
        Users user = new Users();
        user.setId_user(1);
        sensitiveData.setUser(user);

        when(userSensitiveDataRepository.findByEmail(email)).thenReturn(Optional.of(sensitiveData));
        when(usersRepository.findById(1)).thenReturn(Optional.of(user));

        Optional<Users> result = usersService.findUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId_user());
        verify(userSensitiveDataRepository, times(1)).findByEmail(email);
        verify(usersRepository, times(1)).findById(1);
    }

    @Test
    void testAuthenticateUserSuccess() {
        String email = "test@example.com";
        String password = "password123";
        String encodedPassword = "$2a$10$encodedPassword";

        UserSensitiveData sensitiveData = new UserSensitiveData();
        sensitiveData.setEmail(email);
        sensitiveData.setPassword(encodedPassword);

        when(userSensitiveDataRepository.findByEmail(email)).thenReturn(Optional.of(sensitiveData));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        boolean result = usersService.authenticateUser(email, password);

        assertTrue(result);
        verify(userSensitiveDataRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void testAuthenticateUserFailure() {
        String email = "test@example.com";
        String password = "wrongPassword";

        when(userSensitiveDataRepository.findByEmail(email)).thenReturn(Optional.empty());

        boolean result = usersService.authenticateUser(email, password);

        assertFalse(result);
        verify(userSensitiveDataRepository, times(1)).findByEmail(email);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testRegisterUserSuccess() {
        String username = "newUser";
        String email = "new@example.com";
        String password = "password123";

        when(usersRepository.findByNick(username)).thenReturn(Optional.empty());
        when(userSensitiveDataRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(password)).thenReturn("$2a$10$encodedPassword");

        Users savedUser = new Users();
        savedUser.setNick(username);
        savedUser.setId_user(1);

        when(usersRepository.save(any(Users.class))).thenReturn(savedUser);

        String result = usersService.registerUser(username, email, password);

        assertEquals("User registered successfully", result);
        verify(usersRepository, times(1)).findByNick(username);
        verify(userSensitiveDataRepository, times(1)).findByEmail(email);
        verify(usersRepository, times(1)).save(any(Users.class));
        verify(userSensitiveDataRepository, times(1)).save(any(UserSensitiveData.class));
    }

    @Test
    void testRegisterUserFailure() {
        String username = "existingUser";
        String email = "existing@example.com";

        when(usersRepository.findByNick(username)).thenReturn(Optional.of(new Users()));
        when(userSensitiveDataRepository.findByEmail(email)).thenReturn(Optional.empty());

        String result = usersService.registerUser(username, email, "password123");

        assertEquals("User already exists", result);
        verify(usersRepository, times(1)).findByNick(username);
        verify(userSensitiveDataRepository, never()).findByEmail(email);
        verify(usersRepository, never()).save(any(Users.class));
        verify(userSensitiveDataRepository, never()).save(any(UserSensitiveData.class));
    }

    @Test
    void testGetFriendsByUserId() {
        int userId = 1;
        Users friend1 = new Users();
        Users friend2 = new Users();
        List<Users> friends = Arrays.asList(friend1, friend2);

        when(usersRepository.findFriendsByUserId(userId)).thenReturn(friends);

        List<Users> result = usersService.getFriendsByUserId(userId);

        assertEquals(2, result.size());
        verify(usersRepository, times(1)).findFriendsByUserId(userId);
    }
}
