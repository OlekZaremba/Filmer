package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.FriendsList;
import com.filmer.filmerbackend.Entities.UserSensitiveData;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Repositories.FriendsListRepository;
import com.filmer.filmerbackend.Repositories.UserSensitiveDataRepository;
import com.filmer.filmerbackend.Repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendsServiceImplTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private FriendsListRepository friendsListRepository;

    @Mock
    private UserSensitiveDataRepository userSensitiveDataRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private FriendsServiceImpl friendsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetFriendsByUserId() {
        int userId = 1;
        Users friend1 = new Users();
        Users friend2 = new Users();
        List<Users> friends = Arrays.asList(friend1, friend2);

        when(usersRepository.findFriendsByUserId(userId)).thenReturn(friends);

        List<Users> result = friendsService.getFriendsByUserId(userId);

        assertEquals(2, result.size());
        verify(usersRepository, times(1)).findFriendsByUserId(userId);
    }

    @Test
    void testSearchUsersByNick() {
        String nick = "test";
        Users user1 = new Users();
        Users user2 = new Users();
        List<Users> users = Arrays.asList(user1, user2);

        when(usersRepository.findByPartialNick(nick)).thenReturn(users);

        List<Users> result = friendsService.searchUsersByNick(nick);

        assertEquals(2, result.size());
        verify(usersRepository, times(1)).findByPartialNick(nick);
    }

    @Test
    void testAddFriendSuccess() {
        int userId = 1;
        int friendId = 2;

        when(usersRepository.existsById(userId)).thenReturn(true);
        when(usersRepository.existsById(friendId)).thenReturn(true);
        when(friendsListRepository.existsByUser1AndUser2(userId, friendId)).thenReturn(false);

        friendsService.addFriend(userId, friendId);

        verify(usersRepository, times(1)).existsById(userId);
        verify(usersRepository, times(1)).existsById(friendId);
        verify(friendsListRepository, times(1)).existsByUser1AndUser2(userId, friendId);
        verify(friendsListRepository, times(2)).save(any(FriendsList.class));
    }

    @Test
    void testAddFriendSameUser() {
        int userId = 1;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> friendsService.addFriend(userId, userId));

        assertEquals("Nie możesz dodać samego siebie do znajomych.", exception.getMessage());
    }

    @Test
    void testAddFriendUserDoesNotExist() {
        int userId = 1;
        int friendId = 2;

        when(usersRepository.existsById(userId)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> friendsService.addFriend(userId, friendId));

        assertEquals("Jeden z użytkowników nie istnieje.", exception.getMessage());
    }

    @Test
    void testUploadProfilePictureSuccess() throws Exception {
        int userId = 1;
        MultipartFile file = mock(MultipartFile.class);
        Users user = new Users();

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));
        when(file.getBytes()).thenReturn(new byte[]{1, 2, 3});

        friendsService.uploadProfilePicture(userId, file);

        verify(usersRepository, times(1)).save(user);
        assertArrayEquals(new byte[]{1, 2, 3}, user.getProfilePicture());
    }

    @Test
    void testUploadProfilePictureUserNotFound() {
        int userId = 1;
        MultipartFile file = mock(MultipartFile.class);

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> friendsService.uploadProfilePicture(userId, file));

        assertEquals("Użytkownik nie istnieje.", exception.getMessage());
    }

    @Test
    void testGetProfilePictureSuccess() {
        int userId = 1;
        Users user = new Users();
        user.setProfilePicture(new byte[]{1, 2, 3});

        when(usersRepository.findById(userId)).thenReturn(Optional.of(user));

        byte[] result = friendsService.getProfilePicture(userId);

        assertArrayEquals(new byte[]{1, 2, 3}, result);
    }

    @Test
    void testGetProfilePictureUserNotFound() {
        int userId = 1;

        when(usersRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> friendsService.getProfilePicture(userId));

        assertEquals("Użytkownik nie istnieje.", exception.getMessage());
    }

    @Test
    void testSendInviteEmailSuccess() {
        int friendId = 1;
        String lobbyLink = "http://example.com/lobby";

        Users user = new Users();
        UserSensitiveData sensitiveData = new UserSensitiveData();
        sensitiveData.setEmail("friend@example.com");
        user.setUserSensitiveData(sensitiveData);

        when(usersRepository.findById(friendId)).thenReturn(Optional.of(user));
        when(userSensitiveDataRepository.findById(sensitiveData.getId_user_sensitive_data())).thenReturn(Optional.of(sensitiveData));

        friendsService.sendInviteEmail(friendId, lobbyLink);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendInviteEmailUserNotFound() {
        int friendId = 1;
        String lobbyLink = "http://example.com/lobby";

        when(usersRepository.findById(friendId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> friendsService.sendInviteEmail(friendId, lobbyLink));

        assertEquals("Użytkownik nie istnieje.", exception.getMessage());
    }
}
