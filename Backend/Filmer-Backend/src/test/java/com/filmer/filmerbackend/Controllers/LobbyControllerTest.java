package com.filmer.filmerbackend.Controllers;

import com.filmer.filmerbackend.Dtos.PreferencesRequest;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Entities.Users;
import com.filmer.filmerbackend.Services.LobbyService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LobbyControllerDummyTest {

    private final LobbyService lobbyService = mock(LobbyService.class);
    private final LobbyController lobbyController = new LobbyController(lobbyService);

    @Test
    void dummyCreateLobbyTest() {
        when(lobbyService.createLobby(1)).thenReturn(new Lobby());
        lobbyController.createLobby(1);
    }

    @Test
    void dummyCloseLobbyTest() {
        lobbyController.closeLobby(1);
    }

    @Test
    void dummyAddUserToLobbyTest() {
        lobbyController.addUserToLobby("testCode", 1);
    }

    @Test
    void dummyGetParticipantsTest() {
        when(lobbyService.getParticipants("testCode")).thenReturn(Collections.emptyList());
        lobbyController.getParticipants("testCode");
    }

    @Test
    void dummySavePreferencesTest() {
        PreferencesRequest request = new PreferencesRequest();
        lobbyController.savePreferences("testCode", request);
    }

    @Test
    void dummyGetReadyStatusTest() {
        when(lobbyService.getLobbyByCode("testCode")).thenReturn(new Lobby());
        when(lobbyService.areAllUsersReady(1)).thenReturn(true);
        lobbyController.getReadyStatus("testCode");
    }

    @Test
    void dummyStartGameTest() {
        when(lobbyService.getLobbyByCode("testCode")).thenReturn(new Lobby());
        lobbyController.startGame("testCode");
    }

    @Test
    void dummyIsGameStartedTest() {
        Lobby lobby = new Lobby();
        lobby.setStarted(true);
        when(lobbyService.getLobbyByCode("testCode")).thenReturn(lobby);
        lobbyController.isGameStarted("testCode");
    }

    @Test
    void dummyCheckVotingStatusTest() {
        when(lobbyService.checkVotingCompletion("testCode")).thenReturn(true);
        lobbyController.checkVotingStatus("testCode");
    }

    @Test
    void dummyFinishVotingTest() {
        lobbyController.finishVoting("testCode", Map.of("userId", 1));
    }
}
