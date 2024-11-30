package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Users;

import java.util.List;

public interface FriendsService {
    List<Users> searchUsersByNick(String nick);
    List<Users> getFriendsByUserId(int userId);
    void addFriend(int userId, int friendId);
}