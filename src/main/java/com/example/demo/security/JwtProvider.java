package com.example.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtProvider {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;

    public String generateToken(UserDetails userDetails) {
        var issuedDate = new Date();

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(issuedDate)
                .expiration(new Date(issuedDate.getTime() + lifetime.toMillis()))
                .signWith(signingKey())
                .compact();
    }

    public String extractUsername(String token) throws JwtException {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey()).build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey signingKey() {
        var keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
