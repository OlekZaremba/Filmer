package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Dtos.PreferencesRequest;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Services.LobbyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Kontroler REST do zarządzania operacjami związanymi z lobby.
 */
@RestController
@RequestMapping("/lobby/api")
public class LobbyController {

    private final LobbyService lobbyService;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    /**
     * Tworzy nowe lobby.
     *
     * @param ownerId identyfikator właściciela lobby
     * @return obiekt ResponseEntity zawierający utworzone lobby
     */
    @PostMapping("/{ownerId}/create")
    public ResponseEntity<Lobby> createLobby(@PathVariable int ownerId) {
        Lobby lobby = lobbyService.createLobby(ownerId);
        return ResponseEntity.ok(lobby);
    }

    /**
     * Zamyka lobby.
     *
     * @param lobbyId identyfikator lobby
     * @return obiekt ResponseEntity z komunikatem o statusie operacji
     */
    @PostMapping("/{lobbyId}/close")
    public ResponseEntity<String> closeLobby(@PathVariable int lobbyId) {
        lobbyService.closeLobby(lobbyId);
        return ResponseEntity.ok("Lobby zostało zamknięte.");
    }

    /**
     * Dodaje użytkownika do lobby.
     *
     * @param lobbyCode kod lobby
     * @param userId    identyfikator użytkownika
     * @return obiekt ResponseEntity z komunikatem o statusie operacji
     */
    @PostMapping("/{lobbyCode}/addUser/{userId}")
    public ResponseEntity<String> addUserToLobby(@PathVariable String lobbyCode, @PathVariable int userId) {
        lobbyService.addUserToLobby(lobbyCode, userId);
        return ResponseEntity.ok("Użytkownik został dodany do lobby.");
    }

    /**
     * Pobiera listę uczestników lobby.
     *
     * @param lobbyCode kod lobby
     * @return obiekt ResponseEntity zawierający listę uczestników
     */
    @GetMapping("/participants")
    public ResponseEntity<List<Users>> getParticipants(@RequestParam String lobbyCode) {
        List<Users> participants = lobbyService.getParticipants(lobbyCode);
        return ResponseEntity.ok(participants);
    }

    /**
     * Zapisuje preferencje użytkownika w lobby.
     *
     * @param lobbyCode kod lobby
     * @param request   obiekt żądania zawierający preferencje użytkownika
     * @return obiekt ResponseEntity z komunikatem o statusie operacji
     */
    @PostMapping("/{lobbyCode}/preferences")
    public ResponseEntity<Map<String, String>> savePreferences(
            @PathVariable String lobbyCode,
            @RequestBody PreferencesRequest request) {
        try {
            lobbyService.saveUserPreferences(
                    lobbyCode,
                    request.getUserId(),
                    request.getStreamingPlatform(),
                    request.getGenre(),
                    request.getType()
            );
            return ResponseEntity.ok(Map.of("message", "Preferencje zapisane."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Sprawdza, czy wszyscy użytkownicy w lobby są gotowi.
     *
     * @param lobbyCode kod lobby
     * @return obiekt ResponseEntity z informacją o statusie gotowości
     */
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

    /**
     * Rozpoczyna grę w lobby.
     *
     * @param lobbyCode kod lobby
     * @return obiekt ResponseEntity z komunikatem o statusie operacji
     */
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

    /**
     * Sprawdza, czy gra w lobby została rozpoczęta.
     *
     * @param lobbyCode kod lobby
     * @return obiekt ResponseEntity z informacją o statusie gry
     */
    @GetMapping("/{lobbyCode}/is-started")
    public ResponseEntity<Boolean> isGameStarted(@PathVariable String lobbyCode) {
        Lobby lobby = lobbyService.getLobbyByCode(lobbyCode);
        return ResponseEntity.ok(lobby.isStarted());
    }

    /**
     * Sprawdza status głosowania w lobby.
     *
     * @param lobbyCode kod lobby
     * @return obiekt ResponseEntity z informacją o statusie głosowania
     */
    @GetMapping("/{lobbyCode}/status")
    public ResponseEntity<Boolean> checkVotingStatus(@PathVariable String lobbyCode) {
        boolean isCompleted = lobbyService.checkVotingCompletion(lobbyCode);
        return ResponseEntity.ok(isCompleted);
    }

    /**
     * Kończy głosowanie dla użytkownika w lobby.
     *
     * @param lobbyCode kod lobby
     * @param payload   mapa zawierająca identyfikator użytkownika
     * @return obiekt ResponseEntity z komunikatem o statusie operacji
     */
    @PostMapping("/{lobbyCode}/finish-voting")
    public ResponseEntity<String> finishVoting(@PathVariable String lobbyCode, @RequestBody Map<String, Integer> payload) {
        int userId = payload.get("userId");
        lobbyService.finishVoting(lobbyCode, userId);
        return ResponseEntity.ok("Głosowanie zakończone dla użytkownika.");
    }


}
