package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Users;

import java.util.List;

public interface FriendsService {
    List<Users> getFriendsByUserId(int userId);
}
