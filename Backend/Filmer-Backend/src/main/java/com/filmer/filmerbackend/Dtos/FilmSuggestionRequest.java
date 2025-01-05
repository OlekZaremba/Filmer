package com.filmer.filmerbackend.Dtos;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO reprezentujący żądanie sugestii nowego filmu.
 * Zawiera informacje o filmie, takie jak tytuł, opis, gatunek i inne.
 */
@Getter
@Setter
public class FilmSuggestionRequest {

    /**
     * Tytuł filmu.
     */
    private String title;

    /**
     * Opis filmu.
     */
    private String description;

    /**
     * Gatunek filmu.
     */
    private String genre;

    /**
     * Nazwa platformy streamingowej.
     */
    private String platform;

    /**
     * Reżyser filmu.
     */
    private String director;

    /**
     * Studio odpowiedzialne za produkcję filmu.
     */
    private String studio;

    /**
     * Typ filmu.
     */
    private String type;
}
