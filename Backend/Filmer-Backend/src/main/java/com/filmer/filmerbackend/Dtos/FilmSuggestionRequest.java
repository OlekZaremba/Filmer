package com.filmer.filmerbackend.Dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmSuggestionRequest {
    private String title;
    private String description;
    private String genre;
    private String platform;
    private String director;
    private String studio;
    private String type;
}
