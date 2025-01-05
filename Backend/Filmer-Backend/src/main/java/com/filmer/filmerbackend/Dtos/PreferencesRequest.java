package com.filmer.filmerbackend.Dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO reprezentujący żądanie zapisania preferencji użytkownika w kontekście lobby.
 * Zawiera informacje o użytkowniku, preferowanej platformie streamingowej, gatunku i typie filmu.
 */
@Getter
@Setter
public class PreferencesRequest {

    /**
     * Identyfikator użytkownika.
     */
    private int userId;

    /**
     * Preferowana platforma streamingowa.
     */
    private String streamingPlatform;

    /**
     * Preferowany gatunek filmowy.
     */
    private String genre;

    /**
     * Preferowany typ filmu.
     */
    private String type;
}

