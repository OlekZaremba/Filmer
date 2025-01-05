package com.filmer.filmerbackend.Services;

import com.filmer.filmerbackend.Entities.Films;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

/**
 * Interfejs serwisu zarządzającego biblioteką filmów i powiązanymi operacjami.
 */
public interface LibraryService {

    /**
     * Pobiera film na podstawie jego identyfikatora.
     *
     * @param idFilm identyfikator filmu
     * @return obiekt Optional zawierający film, jeśli został znaleziony
     */
    Optional<Films> getFilmById(Integer idFilm);

    /**
     * Generuje plik PDF z informacjami o filmie.
     *
     * @param title       tytuł filmu
     * @param description opis filmu
     * @return obiekt ResponseEntity zawierający plik PDF
     */
    ResponseEntity<byte[]> generatePdf(String title, String description);

    /**
     * Pobiera wszystkie filmy z biblioteki.
     *
     * @return lista wszystkich filmów
     */
    List<Films> getAllFilms();

    /**
     * Pobiera filmy na podstawie gatunku.
     *
     * @param genreName nazwa gatunku
     * @return lista filmów spełniających kryterium gatunku
     */
    List<Films> getFilmsByGenre(String genreName);

    /**
     * Wyszukuje filmy na podstawie ich nazwy.
     *
     * @param name nazwa filmu lub jej część
     * @return lista filmów pasujących do wyszukiwanej frazy
     */
    List<Films> getFilmsByName(String name);

    /**
     * Wysyła sugestię nowego filmu do administratora.
     *
     * @param title       tytuł filmu
     * @param description opis filmu
     * @param genre       gatunek filmu
     * @param platform    platforma, na której film jest dostępny
     * @param director    reżyser filmu
     * @param studio      studio produkcyjne filmu
     * @param type        typ filmu (np. pełnometrażowy, dokumentalny)
     */
    void sendFilmSuggestion(String title, String description, String genre, String platform, String director, String studio, String type);

    /**
     * Pobiera ocenę filmu wystawioną przez użytkownika.
     *
     * @param filmId identyfikator filmu
     * @param userId identyfikator użytkownika
     * @return obiekt Optional zawierający ocenę, jeśli istnieje
     */
    Optional<Integer> getRating(Integer filmId, Integer userId);

    /**
     * Ustawia ocenę filmu wystawioną przez użytkownika.
     *
     * @param filmId identyfikator filmu
     * @param userId identyfikator użytkownika
     * @param rating ocena filmu
     */
    void setRating(Integer filmId, Integer userId, Integer rating);
}

