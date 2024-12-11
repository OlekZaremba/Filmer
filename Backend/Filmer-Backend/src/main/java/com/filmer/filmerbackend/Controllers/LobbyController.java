package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Dtos.PreferencesRequest;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Services.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lobby/api")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @PostMapping("/{ownerId}/create")
    public ResponseEntity<Lobby> createLobby(@PathVariable int ownerId) {
        Lobby lobby = lobbyService.createLobby(ownerId);
        return ResponseEntity.ok(lobby);
    }

    @PostMapping("/{lobbyId}/close")
    public ResponseEntity<String> closeLobby(@PathVariable int lobbyId) {
        lobbyService.closeLobby(lobbyId);
        return ResponseEntity.ok("Lobby zostało zamknięte.");
    }

    @PostMapping("/{lobbyCode}/addUser/{userId}")
    public ResponseEntity<String> addUserToLobby(@PathVariable String lobbyCode, @PathVariable int userId) {
        lobbyService.addUserToLobby(lobbyCode, userId);
        return ResponseEntity.ok("Użytkownik został dodany do lobby.");
    }

    @GetMapping("/participants")
    public ResponseEntity<List<Users>> getParticipants(@RequestParam String lobbyCode) {
        List<Users> participants = lobbyService.getParticipants(lobbyCode);
        return ResponseEntity.ok(participants);
    }

    @PostMapping("/{lobbyCode}/preferences")
    public ResponseEntity<String> savePreferences(@PathVariable String lobbyCode, @RequestBody PreferencesRequest request) {
        try {
            lobbyService.saveUserPreferences(lobbyCode, request.getUserId(), request.getStreamingPlatform(), request.getGenre(), request.getType());
            return ResponseEntity.ok("Preferencje zapisane.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    @GetMapping("/{lobbyId}/ready-status")
    public ResponseEntity<Boolean> getReadyStatus(@PathVariable int lobbyId) {
        boolean allReady = lobbyService.areAllUsersReady(lobbyId);
        return ResponseEntity.ok(allReady);
    }
}
