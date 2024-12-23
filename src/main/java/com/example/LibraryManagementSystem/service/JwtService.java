package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.User;
import com.example.LibraryManagementSystem.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;
@Data
@Service
public class JwtService {  // create a class called JwtService
    // 1st create the secret key(generated from asecuitysite.com)
    private final String SECRET_KEY = "3d500891a245738f4ab04256dc1981a812ceb6e50b0ced50981d2f018f58a12b";

    private final TokenRepository tokenRepository;

    // 6 extract username from the claim
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 7 validate the token
    public boolean isValid(String token, User user) {
        String username = extractUsername(token);

        boolean isValidToken = tokenRepository.findByToken(token).map(t->!t.getLoggedOut()).orElse(false);
        return (username.equals(user.getUsername()))  && !isTokenExpired(token) && isValidToken;
    }

    // 8 check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());

    }

    //9 extract the expiration time, after that create another class for the filter
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    //5th extract a specific claim
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }


    // 4th extract the payload from the token
    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith(getSigninKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    //2nd generate the token
    public String generateToken(User user) {
        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .signWith(getSigninKey())
                .compact();
        return token;
    }

    // 3rd create the signIn key method
    private SecretKey getSigninKey(){
        byte [] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
