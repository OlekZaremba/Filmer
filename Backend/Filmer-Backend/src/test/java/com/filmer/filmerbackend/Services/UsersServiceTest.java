package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Services.UsersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsersServiceTest {

    @Mock
    private UsersService usersService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindUserByNick() {
        String nick = "testNick";
        Users user = new Users();
        user.setNick(nick);

        when(usersService.findUserByNick(nick)).thenReturn(Optional.of(user));

        Optional<Users> result = usersService.findUserByNick(nick);

        assertTrue(result.isPresent());
        assertEquals(nick, result.get().getNick());
        verify(usersService, times(1)).findUserByNick(nick);
    }

    @Test
    void testFindUserByEmail() {
        String email = "test@example.com";
        Users user = new Users();
        user.setId_user(1);

        when(usersService.findUserByEmail(email)).thenReturn(Optional.of(user));

        Optional<Users> result = usersService.findUserByEmail(email);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getId_user());
        verify(usersService, times(1)).findUserByEmail(email);
    }

    @Test
    void testAuthenticateUser() {
        String email = "test@example.com";
        String password = "password123";

        when(usersService.authenticateUser(email, password)).thenReturn(true);

        boolean isAuthenticated = usersService.authenticateUser(email, password);

        assertTrue(isAuthenticated);
        verify(usersService, times(1)).authenticateUser(email, password);
    }

    @Test
    void testRegisterUser() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password123";

        when(usersService.registerUser(username, email, password)).thenReturn("User registered successfully");

        String result = usersService.registerUser(username, email, password);

        assertEquals("User registered successfully", result);
        verify(usersService, times(1)).registerUser(username, email, password);
    }

    @Test
    void testGetFriendsByUserId() {
        int userId = 1;
        Users friend = new Users();
        friend.setNick("friendNick");

        when(usersService.getFriendsByUserId(userId)).thenReturn(List.of(friend));

        List<Users> friends = usersService.getFriendsByUserId(userId);

        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertEquals("friendNick", friends.get(0).getNick());
        verify(usersService, times(1)).getFriendsByUserId(userId);
    }
}
