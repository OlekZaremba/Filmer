package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Services.ResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultsController {

    private final ResultsService resultsService;

    @GetMapping("/{lobbyCode}")
    public ResponseEntity<Map<Integer, List<Films>>> getResults(@PathVariable String lobbyCode) {
        Map<Integer, List<Films>> results = resultsService.getResultsByLobbyCode(lobbyCode);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/{lobbyCode}/sendEmail")
    public ResponseEntity<String> sendResultsEmail(@PathVariable String lobbyCode, @RequestParam String email) {
        try {
            resultsService.sendResultsEmail(lobbyCode, email);
            return ResponseEntity.ok("Wyniki zostały wysłane na e-mail: " + email);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Nie udało się wysłać wyników: " + e.getMessage());
        }
    }

}
