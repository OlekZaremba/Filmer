package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Services.DrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/draw")
@RequiredArgsConstructor
public class DrawController {

    private final DrawService drawService;

    @PostMapping("/{lobbyCode}/start")
    public ResponseEntity<List<Films>> startDraw(@PathVariable String lobbyCode) {
        List<Films> films = drawService.drawFilms(lobbyCode);
        return ResponseEntity.ok(films);
    }

    @PostMapping("/{lobbyCode}/vote/{filmId}")
    public ResponseEntity<?> submitVote(@PathVariable String lobbyCode, @PathVariable Integer filmId, @RequestParam Integer userId) {
        drawService.submitVote(lobbyCode, filmId, userId);
        return ResponseEntity.ok(Collections.singletonMap("message", "Głos zapisany"));
    }



    @GetMapping("/{lobbyCode}/results")
    public ResponseEntity<List<Object[]>> getResults(@PathVariable String lobbyCode) {
        List<Object[]> results = drawService.getResults(lobbyCode);
        return ResponseEntity.ok(results);
    }
}
