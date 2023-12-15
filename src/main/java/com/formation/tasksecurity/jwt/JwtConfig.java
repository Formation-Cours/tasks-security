package com.formation.tasksecurity.jwt;

import com.formation.tasksecurity.configurations.MyUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Slf4j
@Component
public class JwtConfig {

    @Value("${jwt.secret:samsamsam}")
    private String secret;

    @Value("${jwt.expiration:#{24*60*60*1000}}")
    private long expirationTime;

    private SecretKey key;

    private final UserDetailsService userDetailsService;

    public JwtConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Map<String, String> generateToken(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return Map.of("Bearer", generateJwt((MyUserDetails) userDetails));
    }

    private String generateJwt(MyUserDetails userDetails) {
        // heure création (ms)
        final long currentTime = System.currentTimeMillis();

        expirationTime += currentTime;

        Map<String, Object> claims = Map.of(
                "email", userDetails.getUsername(),
                "firstName", userDetails.getUser().getFirstName(),
                "lastName", userDetails.getUser().getLastName()
        );

        return Jwts.builder()
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expirationTime))
                .subject(userDetails.getUsername())
                .claims(claims)
                .signWith(key)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return (Claims) Jwts.parser()
                .verifyWith(key)
                .build()
                .parse(token)
                .getPayload();
    }
}
