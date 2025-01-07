package com.reverb.app.configs;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenUtil {

    private final SecretKey jwtSecret;

    public JwtTokenUtil(AppProperties properties) {
        this.jwtSecret = Keys.hmacShaKeyFor(properties.getJwt().getSecret().getBytes());
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            throw new RuntimeException("Invalid JWT Token");
        }
    }

    public boolean validateToken(String token, String username) {
        Claims claims = parseToken(token);
        return claims.getSubject().equals(username) && !isTokenExpired(claims);
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}