package com.filmer.filmerbackend.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreferencesRequest {
    private int userId;
    private String streamingPlatform;
    private String genre;
    private String type;
}

