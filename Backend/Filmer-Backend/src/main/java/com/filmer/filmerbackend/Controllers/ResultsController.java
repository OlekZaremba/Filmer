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

}
