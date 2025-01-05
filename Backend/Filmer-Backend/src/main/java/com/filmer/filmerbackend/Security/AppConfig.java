package com.filmer.filmerbackend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Klasa konfiguracyjna dla aplikacji.
 * Definiuje beany związane z bezpieczeństwem aplikacji.
 */
@Configuration
public class AppConfig {

    /**
     * Bean odpowiedzialny za enkodowanie haseł użytkowników.
     *
     * @return instancja {@link BCryptPasswordEncoder}
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}