package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.User;
import com.example.LibraryManagementSystem.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {  // JwtService handles JWT token operations

    @Value("${jwt.secret}") // Injected from application.properties or environment variable
    private String SECRET_KEY;

    private final TokenRepository tokenRepository;

    /**
     * Extract username from the JWT token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extract a specific claim using a resolver function.
     */
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    /**
     * Extract all claims from the token.
     */
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Check if the token has expired.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extract expiration date from the token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Validate the JWT token.
     */
    public boolean isValid(String token) {
        try {
            String username = extractUsername(token);
            User user = tokenRepository.findByToken(token)
                    .map(t -> t.getUser())
                    .orElse(null);
            return (username != null && user != null && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generate a JWT token for the user.
     */
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole().name()) // Include role in token
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24 hours
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Get the signing key using the secret key.
     */
    private SecretKey getSigningKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
