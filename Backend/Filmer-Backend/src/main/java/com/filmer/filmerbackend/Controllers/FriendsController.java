package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Services.FriendsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friends/api")
public class FriendsController {

    private final FriendsService friendsService;

    public FriendsController(FriendsService friendsService) {
        this.friendsService = friendsService;
    }

    @GetMapping("/{userId}/list")
    public ResponseEntity<List<Users>> getFriends(@PathVariable int userId) {
        List<Users> friends = friendsService.getFriendsByUserId(userId);
        return ResponseEntity.ok(friends);
    }
}
