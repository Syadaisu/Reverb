package com.reverb.app.services;

import com.reverb.app.configs.AppProperties;
import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import javax.crypto.SecretKey;

@Service
public class AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final SecretKey jwtSecret;
    private final String jwtIssuer;
    private final String jwtAudience;

    @Autowired
    public AccountService(UserRepository userRepository,
                          PasswordEncoder passwordEncoder, AppProperties properties) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecret = Keys.hmacShaKeyFor(properties.getJwt().getSecret().getBytes());
        this.jwtIssuer = properties.getJwt().getIssuer();
        this.jwtAudience = properties.getJwt().getAudience();
    }

    public CompletableFuture<Void> register(String email, String username, String password) {
        return CompletableFuture.runAsync(() -> {
            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("User with this email already exists");
            }

            User user = new User();
            user.setEmail(email);
            user.setUserName(username);
            user.setPassword(passwordEncoder.encode(password));
            user.setCreatedAt(new Date());
            userRepository.save(user);
        });
    }

    public CompletableFuture<Map<String, Object>> login(String email, String password) {
        System.out.println("AccountServiceLogin");
        return CompletableFuture.supplyAsync(() -> {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid email or password");
            }
            System.out.println("DatabaseCheckDone");
            String accessToken = generateAccessToken(user);
            String refreshToken = generateRefreshToken(String.valueOf(user.getUserId()));
            System.out.println("TokenGenDone");
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            System.out.println("ResponseDone " + response);
            return response;
        });
    }

    public String generateAccessToken(User user) {


        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .claim("username", user.getUserName())
                .claim("email", user.getEmail())
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(jwtSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .claim("type", "refresh")
                .setIssuer(jwtIssuer)
                .setAudience(jwtAudience)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 259200000)) // 3 days
                .signWith(jwtSecret, SignatureAlgorithm.HS256)
                .compact();
    }

    public CompletableFuture<Map<String, String>> refreshToken(String token) {
        return CompletableFuture.supplyAsync(() -> {
            Claims claims;
            try {
                claims = Jwts.parser()
                        .setSigningKey(jwtSecret)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
            } catch (JwtException e) {
                throw new RuntimeException("Invalid refresh token");
            }

            if (!"refresh".equals(claims.get("type"))) {
                throw new RuntimeException("Invalid refresh token type");
            }

            int userId = Integer.parseInt(claims.getSubject());
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String newAccessToken = generateAccessToken(user);
            String newRefreshToken = generateRefreshToken(String.valueOf(userId));

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", newAccessToken);
            tokens.put("refreshToken", newRefreshToken);
            return tokens;
        });
    }
}
