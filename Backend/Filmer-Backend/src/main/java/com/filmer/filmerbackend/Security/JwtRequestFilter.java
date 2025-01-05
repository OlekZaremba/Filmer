package com.filmer.filmerbackend.Security;

import com.filmer.filmerbackend.Services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Filtr obsługujący żądania przychodzące w celu uwierzytelnienia użytkownika za pomocą tokena JWT.
 * Jest uruchamiany raz dla każdego żądania HTTP.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsersService userService;

    /**
     * Przetwarza każde żądanie HTTP, sprawdzając nagłówek autoryzacji pod kątem tokena JWT
     * i ustawia uwierzytelnienie użytkownika, jeśli token jest poprawny.
     *
     * @param request  żądanie HTTP
     * @param response odpowiedź HTTP
     * @param chain    łańcuch filtrów
     * @throws ServletException w przypadku błędu przetwarzania
     * @throws IOException      w przypadku błędu wejścia/wyjścia
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            if (jwtUtil.validateToken(jwt, username)) {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        username, null, new ArrayList<>());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
