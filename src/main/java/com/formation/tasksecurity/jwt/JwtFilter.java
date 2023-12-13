package com.formation.tasksecurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.tasksecurity.configurations.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final MyUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtFilter(JwtConfig jwtConfig, MyUserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Je suis dans le filtre JwtFilter");

        try {
            String token = request.getHeader("Authorization");

            log.info("token {}", token);

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                if (isTokenExpired(token)) {
                    throw new ExpiredJwtException(null, null, "Token expiré");
                }

                String username = getUsername(token);
                if (username == null) {
                    throw new RuntimeException("Token invalide");
                }

                setAuthentication(username);

            } else {
                log.info("Token absent");
            }


        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", e.getMessage())));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", e.getMessage())));
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenExpired(String token) {
        return jwtConfig.isTokenExpired(token);
    }

    private String getUsername(String token) {
        String username = jwtConfig.extractUsername(token);
        if (username != null) {
            return username;
        }
        return null;
    }

    private void setAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            ));
        }
    }
}
