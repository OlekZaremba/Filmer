package com.filmer.filmerbackend.Dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO reprezentujący żądanie wysłania zaproszenia do lobby.
 * Zawiera informacje o użytkowniku, do którego ma być wysłane zaproszenie, oraz link do lobby.
 */
@Getter
@Setter
public class SendInviteRequest {

    /**
     * Identyfikator znajomego, do którego ma być wysłane zaproszenie.
     */
    private int friendId;

    /**
     * Link do lobby.
     */
    private String lobbyLink;
}
