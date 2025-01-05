package com.filmer.filmerbackend.ServicesImpl;

import com.filmer.filmerbackend.Entities.Films;
import com.filmer.filmerbackend.Entities.Lobby;
import com.filmer.filmerbackend.Repositories.LobbyRepository;
import com.filmer.filmerbackend.Repositories.LobbyResultsRepository;
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

/**
 * Klasa testowa dla usługi {@link ResultsServiceImpl}.
 * Testuje funkcjonalności związane z pobieraniem wyników głosowań oraz wysyłaniem e-maili z wynikami.
 */
@ExtendWith(MockitoExtension.class)
public class ResultsServiceImplTest {

    @Mock
    private LobbyRepository lobbyRepository;

    @Mock
    private LobbyResultsRepository lobbyResultsRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private ResultsServiceImpl resultsService;

    /**
     * Testuje metodę {@link ResultsServiceImpl#getResultsByLobbyCode(String)}.
     * <p>
     * Scenariusz: Pobranie wyników głosowania dla poprawnego kodu lobby.
     * Oczekiwany wynik: Zwrócenie mapy wyników z odpowiednimi filmami i liczbą głosów.
     */
    @Test
    public void getResultsByLobbyCode_ShouldReturnCorrectResults() {
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

        Map<Integer, List<Films>> results = resultsService.getResultsByLobbyCode(lobbyCode);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.get(1).contains(film1));
        assertTrue(results.get(2).contains(film2));
    }

    /**
     * Testuje metodę {@link ResultsServiceImpl#sendResultsEmail(String, String)}.
     * <p>
     * Scenariusz: Wysłanie e-maila z wynikami głosowania dla istniejącego lobby i poprawnego adresu e-mail.
     * Oczekiwany wynik: E-mail zostaje wysłany z odpowiednimi wynikami w treści.
     */
    @Test
    public void sendResultsEmail_ShouldSendEmail_WhenLobbyAndResultsExist() {
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

        resultsService.sendResultsEmail(lobbyCode, email);

        verify(mailSender).send(emailCaptor.capture());
        SimpleMailMessage capturedMessage = emailCaptor.getValue();

        assertEquals(email, capturedMessage.getTo()[0]);
        assertEquals("Wyniki głosowania w lobby: " + lobbyCode, capturedMessage.getSubject());
        assertTrue(capturedMessage.getText().contains("Miejsce 1:\n - Film 1"));
        assertTrue(capturedMessage.getText().contains("Miejsce 2:\n - Film 2"));
    }

    /**
     * Testuje metodę {@link ResultsServiceImpl#sendResultsEmail(String, String)}.
     * <p>
     * Scenariusz: Próba wysłania e-maila, gdy występuje błąd wysyłania.
     * Oczekiwany wynik: Rzucenie wyjątku {@link RuntimeException} z odpowiednim komunikatem.
     */
    @Test
    public void sendResultsEmail_ShouldThrowException_WhenEmailSendingFails() {
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

        Exception exception = assertThrows(RuntimeException.class, () -> resultsService.sendResultsEmail(lobbyCode, email));
        assertTrue(exception.getMessage().contains("Błąd podczas wysyłania e-maila"));
    }

    /**
     * Testuje metodę {@link ResultsServiceImpl#getResultsByLobbyCode(String)}.
     * <p>
     * Scenariusz: Próba pobrania wyników dla nieistniejącego lobby.
     * Oczekiwany wynik: Rzucenie wyjątku {@link IllegalArgumentException} z komunikatem o braku lobby.
     */
    @Test
    public void getResultsByLobbyCode_ShouldThrowException_WhenLobbyDoesNotExist() {
        // Given
        String lobbyCode = "nonExistentLobby";
        when(lobbyRepository.findByLobbyCode(lobbyCode)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> resultsService.getResultsByLobbyCode(lobbyCode));
        assertTrue(exception.getMessage().contains("Lobby nie istnieje."));
    }
}