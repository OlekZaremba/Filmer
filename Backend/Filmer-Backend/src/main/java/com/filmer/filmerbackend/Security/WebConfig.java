package com.filmer.filmerbackend.Security;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Klasa konfiguracyjna dla ustawień CORS (Cross-Origin Resource Sharing).
 * Pozwala na komunikację między backendem a frontendem na różnych domenach.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Dodaje reguły CORS umożliwiające frontendowi komunikację z backendem.
     *
     * @param registry rejestracja reguł CORS
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}