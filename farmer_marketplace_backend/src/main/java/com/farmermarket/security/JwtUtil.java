package com.farmermarket.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET_KEY =
            "farmer_market_secret_key_farmer_market_secret_key";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 10; // 10 hours

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // ✅ Generate JWT Token
    public String generateToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    // ✅ Extract username from token
    public String extractUsername(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Validate token (VERY IMPORTANT)
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token); // checks expiry + signature
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
