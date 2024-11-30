package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Services.FriendsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{userId}/addFriend/{friendId}")
    public ResponseEntity<String> addFriend(@PathVariable int userId, @PathVariable int friendId) {
        try {
            friendsService.addFriend(userId, friendId);
            return ResponseEntity.ok("Użytkownik został dodany do znajomych.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Users>> searchUsers(@RequestParam String nick) {
        List<Users> users = friendsService.searchUsersByNick(nick);
        return ResponseEntity.ok(users);
    }
}
