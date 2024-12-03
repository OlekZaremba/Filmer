package com.filmer.filmerbackend.Controllers;

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
}
