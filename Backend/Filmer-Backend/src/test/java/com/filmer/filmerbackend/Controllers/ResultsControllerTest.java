package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Services.ResultsService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResultsControllerDummyTest {

    private final ResultsService resultsService = mock(ResultsService.class);
    private final ResultsController resultsController = new ResultsController(resultsService);

    @Test
    void dummyGetResultsTest() {
        when(resultsService.getResultsByLobbyCode("testCode")).thenReturn(Map.of(1, Collections.emptyList()));
        resultsController.getResults("testCode");
    }

    @Test
    void dummySendResultsEmailTest() {
        resultsController.sendResultsEmail("testCode", "test@example.com");
    }
}
