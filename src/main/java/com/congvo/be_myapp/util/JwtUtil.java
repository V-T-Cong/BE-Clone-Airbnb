package com.congvo.be_myapp.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    public String extractEmailFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 1. Get the signing key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 2. Extract a single claim (e.g., username)
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 3. Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // --- Public Methods ---

    /**
     * Generates a JWT for the given username.
     * @param username The subject of the token (the user).
     * @return The signed JWT string.
     */
    public String generateToken(String username) {
        // 1. Prepare Claims and Dates
        Map<String, Object> claims = new HashMap<>();
        // The Claims map can be populated with custom data/roles

        final long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiration = new Date(now + jwtExpiration);

        // 2. Build the Token using the modern API
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Validates if the token is valid (not expired and signature is correct).
     * @param token The JWT string.
     * @param username The expected username (from DB or security context).
     * @return true if valid, false otherwise.
     */
    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractClaim(token, Claims::getSubject);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    /**
     * Checks if the token's expiration date is in the past.
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    /**
     * Extracts the username (subject) from a token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

}
