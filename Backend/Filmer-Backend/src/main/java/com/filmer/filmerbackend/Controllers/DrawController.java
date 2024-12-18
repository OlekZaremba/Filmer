package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Services.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/draw")
@RequiredArgsConstructor
public class DrawController {

    private final DrawService drawService;

    @PostMapping("/{lobbyId}/start")
    public ResponseEntity<List<Films>> startDraw(@PathVariable Integer lobbyId) {
        List<Films> films = drawService.drawFilms(lobbyId);
        return ResponseEntity.ok(films);
    }

    @PostMapping("/{lobbyId}/vote/{filmId}")
    public ResponseEntity<String> submitVote(@PathVariable Integer lobbyId, @PathVariable Integer filmId) {
        drawService.submitVote(lobbyId, filmId);
        return ResponseEntity.ok("Głos został zapisany.");
    }

    @GetMapping("/{lobbyId}/results")
    public ResponseEntity<List<Object[]>> getResults(@PathVariable Integer lobbyId) {
        List<Object[]> results = drawService.getResults(lobbyId);
        return ResponseEntity.ok(results);
    }
}
