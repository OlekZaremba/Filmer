package com.filmer.filmerbackend.Security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Klasa konfiguracyjna dla ustawień bezpieczeństwa aplikacji.
 * Konfiguruje zabezpieczenia HTTP oraz integrację z filtrem JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * Konstruktor przyjmujący filtr JWT.
     *
     * @param jwtRequestFilter filtr do uwierzytelniania za pomocą tokenów JWT
     */
    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    /**
     * Konfiguruje łańcuch zabezpieczeń HTTP.
     *
     * @param http obiekt konfiguracji HTTP
     * @return skonfigurowany łańcuch filtrów zabezpieczeń
     * @throws Exception w przypadku błędu konfiguracji
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
