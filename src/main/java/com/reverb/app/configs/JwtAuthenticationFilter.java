package com.reverb.app.configs;

import com.reverb.app.models.User;
import com.reverb.app.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        String token = null;
        Integer userId = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
            try {
                Claims claims = jwtTokenUtil.parseToken(token);
                userId = Integer.parseInt(claims.getSubject());

                List<String> roles = claims.get("roles", List.class);
                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role)) // Ensure roles have "ROLE_" prefix if needed
                        .collect(Collectors.toList());

                System.out.println("Token: " + token);
                System.out.println("UserID from token: " + userId + " Roles: " + authorities);

            } catch (RuntimeException e) {
                System.out.println("Invalid JWT Token: " + token);
            }
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = null;
            try {
                user = userRepository.findByUserId(userId)
                        .orElseThrow(() -> new Exception("Authenticated user not found"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (user != null && jwtTokenUtil.validateToken(token, userId.toString())) {
                System.out.println("User found: " + user.getUserName());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authentication set in SecurityContext");
            } else {
                System.out.println("User not found or token invalid");
            }
        }

        System.out.println("Authentication after filter: " + SecurityContextHolder.getContext().getAuthentication());
        chain.doFilter(request, response);
    }
}
