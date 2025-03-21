package com.reverb.app.services;

import com.reverb.app.configs.AppProperties;
import com.reverb.app.models.Attachment;
import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import java.util.stream.Collectors;

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

    public CompletableFuture<User> register(String email, String userName, String password) {
        return CompletableFuture.supplyAsync(() -> {
            if (userRepository.existsByEmail(email)) {
                throw new RuntimeException("User with this email already exists");
            }
            User user = new User();
            user.setEmail(email);
            user.setUserName(userName);
            user.setPassword(passwordEncoder.encode(password));
            user.setCreatedAt(new Date());
            user.setAuthorities(Collections.singletonList("ROLE_USER"));
            return userRepository.save(user);
        });
    }

    public CompletableFuture<Map<String, Object>> login(String email, String password) {
        System.out.println("AccountServiceLogin");
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("DatabaseCheck " + email + " " + password);
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid email or password"));

            if (!passwordEncoder.matches(password, user.getPassword())) {
                throw new RuntimeException("Invalid email or password");
            }
            System.out.println("DatabaseCheckDone");
            String accessToken = generateAccessToken(user);
            String refreshToken = generateRefreshToken(String.valueOf(user.getUserId()));
            System.out.println("TokenGenDone");
            Attachment avatar = user.getAvatar() != null ? user.getAvatar() : null;
            String avatarUuid = null;
            if (avatar != null) {
                avatarUuid = avatar.getAttachmentUuid();
            }
            Map<String, Object> response = new HashMap<>();
            response.put("user", user);
            response.put("accessToken", accessToken);
            response.put("refreshToken", refreshToken);
            response.put("avatarUuid", avatarUuid); ;
            System.out.println("ResponseDone " + response);

            return response;
        });
    }

    public String generateAccessToken(User user) {
        System.out.println("SecretKey" + jwtSecret);
        return Jwts.builder()
                .setSubject(String.valueOf(user.getUserId()))
                .claim("username", user.getUserName())
                .claim("email", user.getEmail())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
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
                        .parseSignedClaims(token)
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

    public boolean validateToken(String token, User user) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .build()
                    .parseSignedClaims(token)
                    .getBody();
            String userName = claims.getSubject();
            System.out.println("Validaton: " + userName + "Claims: " + claims);
            return (userName.equals(user.getUserName()) && !isTokenExpired(claims));
        } catch (JwtException | IllegalArgumentException e) {
            System.out.println("Invalid token: " + e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    public SecretKey getJwtSecret() {
        return jwtSecret;
    }
}