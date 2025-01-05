package com.filmer.filmerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa aplikacji backendowej.
 * Punkt wejścia dla aplikacji Spring Boot.
 */
@SpringBootApplication
public class FilmerBackendApplication {

    /**
     * Metoda główna uruchamiająca aplikację.
     *
     * @param args argumenty przekazane podczas uruchamiania aplikacji
     */
    public static void main(String[] args) {
        SpringApplication.run(FilmerBackendApplication.class, args);
    }
}
