package com.practiceProject.ecommece.config;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtProvider {

    // Create a secret key for signing JWTs
    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

    public String generateToken(Authentication auth) {
        return Jwts.builder()
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date(new Date().getTime() + 846000000)) // Expiration time (10 days)
                .claim("email", auth.getName()) // Store user email in the token
                .signWith(key) // Sign with secret key
                .compact(); // Build and return token
    }

    public String getEmailFromToken(String Jwt) {
        Jwt = Jwt.substring(7); // Remove "Bearer " prefix
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key) // Set secret key
                .build()
                .parseClaimsJws(Jwt) // Validate token
                .getBody(); // Extract payload

        return String.valueOf(claims.get("email")); // Return extracted email
    }
}