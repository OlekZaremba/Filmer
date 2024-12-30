package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Repositories.LobbyRepository;
import com.filmer.filmerbackend.Repositories.LobbyResultsRepository;
import com.filmer.filmerbackend.Services.ResultsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultsServiceImplTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private LobbyResultsRepository lobbyResultsRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private ResultsServiceImpl resultsService;

    @Test
    void getResultsByLobbyCode_ShouldReturnCorrectResults() {
        // Given
        String lobbyCode = "testLobbyCode";
        Lobby lobby = new Lobby();
        lobby.setIdLobby(1);
        lobby.setLobbyCode(lobbyCode);

        Films film1 = new Films();
        film1.setFilmName("Film 1");

        Films film2 = new Films();
        film2.setFilmName("Film 2");

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyResultsRepository.countVotesByLobby(lobby.getIdLobby()))
                .thenReturn(Arrays.asList(new Object[]{film1, 1}, new Object[]{film2, 2}));

        // When
        Map<Integer, List<Films>> results = resultsService.getResultsByLobbyCode(lobbyCode);

        // Then
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.get(1).contains(film1));
        assertTrue(results.get(2).contains(film2));
    }

    @Test
    void sendResultsEmail_ShouldSendEmail_WhenLobbyAndResultsExist() {
        // Given
        String lobbyCode = "testLobbyCode";
        String email = "test@example.com";
        Lobby lobby = new Lobby();
        lobby.setIdLobby(1);
        lobby.setLobbyCode(lobbyCode);

        Films film1 = new Films();
        film1.setFilmName("Film 1");

        Films film2 = new Films();
        film2.setFilmName("Film 2");

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyResultsRepository.countVotesByLobby(lobby.getIdLobby()))
                .thenReturn(Arrays.asList(new Object[]{film1, 1}, new Object[]{film2, 2}));

        ArgumentCaptor<SimpleMailMessage> emailCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);

        // When
        resultsService.sendResultsEmail(lobbyCode, email);

        // Then
        verify(mailSender).send(emailCaptor.capture());
        SimpleMailMessage capturedMessage = emailCaptor.getValue();

        assertEquals(email, capturedMessage.getTo()[0]);
        assertEquals("Wyniki głosowania w lobby: " + lobbyCode, capturedMessage.getSubject());
        assertTrue(capturedMessage.getText().contains("Miejsce 1:\n - Film 1"));
        assertTrue(capturedMessage.getText().contains("Miejsce 2:\n - Film 2"));
    }

    @Test
    void sendResultsEmail_ShouldThrowException_WhenEmailSendingFails() {
        // Given
        String lobbyCode = "testLobbyCode";
        String email = "test@example.com";
        Lobby lobby = new Lobby();
        lobby.setIdLobby(1);
        lobby.setLobbyCode(lobbyCode);

        Films film1 = new Films();
        film1.setFilmName("Film 1");

        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.of(lobby));
        when(lobbyResultsRepository.countVotesByLobby(lobby.getIdLobby())).thenReturn(
                (List<Object[]>) (List<?>) Arrays.asList(new Object[]{film1, 1})
        );

        doThrow(new RuntimeException("Email error")).when(mailSender).send(any(SimpleMailMessage.class));

        // When & Then
        Exception exception = assertThrows(RuntimeException.class, () -> resultsService.sendResultsEmail(lobbyCode, email));
        assertTrue(exception.getMessage().contains("Błąd podczas wysyłania e-maila"));
    }

    @Test
    void getResultsByLobbyCode_ShouldThrowException_WhenLobbyDoesNotExist() {
        // Given
        String lobbyCode = "nonExistentLobby";
        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.empty());

        // When & Then
        Exception exception = assertThrows(IllegalArgumentException.class, () -> resultsService.getResultsByLobbyCode(lobbyCode));
        assertTrue(exception.getMessage().contains("Lobby nie istnieje."));
    }
}