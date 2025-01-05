package com.filmer.filmerbackend.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Narzędzie do obsługi tokenów JWT.
 * Umożliwia generowanie, weryfikację i wyodrębnianie informacji z tokenów JWT.
 */
@Component
public class JwtUtil {

    /**
     * Sekretny klucz używany do podpisywania tokenów JWT.
     */
    private String SECRET_KEY = "your_secret_key";

    /**
     * Wyodrębnia nazwę użytkownika (subject) z tokena JWT.
     *
     * @param token token JWT
     * @return nazwa użytkownika
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Wyodrębnia datę wygaśnięcia tokena JWT.
     *
     * @param token token JWT
     * @return data wygaśnięcia
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Wyodrębnia dowolne roszczenie (claim) z tokena JWT na podstawie przekazanej funkcji.
     *
     * @param token          token JWT
     * @param claimsResolver funkcja do wyodrębnienia roszczenia
     * @param <T>            typ wartości roszczenia
     * @return wyodrębnione roszczenie
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generuje token JWT dla danego użytkownika.
     *
     * @param username nazwa użytkownika
     * @return wygenerowany token JWT
     */
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * Waliduje token JWT na podstawie nazwy użytkownika i daty wygaśnięcia.
     *
     * @param token    token JWT
     * @param username nazwa użytkownika
     * @return true, jeśli token jest ważny; false w przeciwnym razie
     */
    public Boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }
}
