package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Services.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * Kontroler REST do zarządzania losowaniem filmów i głosowaniem.
 */
@RestController
@RequestMapping("/api/draw")
@RequiredArgsConstructor
public class DrawController {

    private final DrawService drawService;

    /**
     * Rozpoczyna losowanie filmów dla lobby.
     *
     * @param lobbyCode kod lobby
     * @return obiekt ResponseEntity zawierający listę wylosowanych filmów
     */
    @PostMapping("/{lobbyCode}/start")
    public ResponseEntity<List<Films>> startDraw(@PathVariable String lobbyCode) {
        List<Films> films = drawService.drawFilms(lobbyCode);
        return ResponseEntity.ok(films);
    }

    /**
     * Zapisuje głos użytkownika na dany film.
     *
     * @param lobbyCode kod lobby
     * @param filmId    identyfikator filmu
     * @param userId    identyfikator użytkownika
     * @return obiekt ResponseEntity z komunikatem o statusie operacji
     */
    @PostMapping("/{lobbyCode}/vote/{filmId}")
    public ResponseEntity<?> submitVote(@PathVariable String lobbyCode, @PathVariable Integer filmId, @RequestParam Integer userId) {
        drawService.submitVote(lobbyCode, filmId, userId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Głos zapisany"));
    }

}
