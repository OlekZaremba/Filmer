package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Users;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FriendsService {
    List<Users> searchUsersByNick(String nick);
    List<Users> getFriendsByUserId(int userId);
    void addFriend(int userId, int friendId);
    void uploadProfilePicture(int userId, MultipartFile file);
    byte[] getProfilePicture(int userId);
    void sendInviteEmail(int friendId, String lobbyLink);
}
