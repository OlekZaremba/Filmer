package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Dtos.PreferencesRequest;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Services.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping("/{lobbyCode}/ready-status")
    public ResponseEntity<Boolean> getReadyStatus(@PathVariable String lobbyCode) {
        try {
            Lobby lobby = lobbyService.getLobbyByCode(lobbyCode);
            boolean allReady = lobbyService.areAllUsersReady(lobby.getIdLobby());
            return ResponseEntity.ok(allReady);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }

    @PostMapping("/{lobbyCode}/start")
    public ResponseEntity<String> startGame(@PathVariable String lobbyCode) {
        try {
            Lobby lobby = lobbyService.getLobbyByCode(lobbyCode);
            lobbyService.startGame(lobby.getIdLobby());
            return ResponseEntity.ok("Gra została rozpoczęta.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{lobbyCode}/is-started")
    public ResponseEntity<Boolean> isGameStarted(@PathVariable String lobbyCode) {
        Lobby lobby = lobbyService.getLobbyByCode(lobbyCode);
        return ResponseEntity.ok(lobby.isStarted());
    }

    @GetMapping("/{lobbyCode}/status")
    public ResponseEntity<Boolean> checkVotingStatus(@PathVariable String lobbyCode) {
        boolean isCompleted = lobbyService.checkVotingCompletion(lobbyCode);
        return ResponseEntity.ok(isCompleted);
    }

    @PostMapping("/{lobbyCode}/finish-voting")
    public ResponseEntity<String> finishVoting(@PathVariable String lobbyCode, @RequestBody Map<String, Integer> payload) {
        int userId = payload.get("userId");
        lobbyService.finishVoting(lobbyCode, userId);
        return ResponseEntity.ok("Głosowanie zakończone dla użytkownika.");
    }


}
