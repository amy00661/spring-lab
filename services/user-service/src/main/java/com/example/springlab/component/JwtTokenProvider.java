package com.example.springlab.component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String JWT_SECRET = "yourSecretKey"; // TODO: 放在 application.yml
    private final long JWT_EXPIRATION = 86400000; // 1 天

    public String generateToken(org.springframework.security.core.userdetails.UserDetails principal,
                                String JWT_SECRET,
                                long JWT_EXPIRATION) {

        SecretKey key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();
        Instant exp = now.plusMillis(JWT_EXPIRATION);

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key, SignatureAlgorithm.HS512)   // ✅ 0.11.5 用這個
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (JwtException ex) {
            return false;
        }
    }
}
