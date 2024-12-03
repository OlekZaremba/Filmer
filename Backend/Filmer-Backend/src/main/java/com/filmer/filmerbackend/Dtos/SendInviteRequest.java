package com.filmer.filmerbackend.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendInviteRequest {
    private int friendId;
    private String lobbyLink;
}
