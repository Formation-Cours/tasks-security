package com.formation.tasksecurity.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formation.tasksecurity.exceptions.ResponseHandler;
import com.formation.tasksecurity.jwt.JwtFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtFilter jwtFilter;

    private final ObjectMapper objectMapper;

    public SecurityConfiguration(JwtFilter jwtFilter, ObjectMapper objectMapper) {
        this.jwtFilter = jwtFilter;
        this.objectMapper = objectMapper;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests ->
                        requests
                                .requestMatchers(HttpMethod.POST, "/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                                .requestMatchers( "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint((req, resp, excep) -> {
                                    resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    resp.setContentType("application/json");
                                    ResponseHandler responseHandler = new ResponseHandler();
                                    responseHandler.status = HttpServletResponse.SC_UNAUTHORIZED;
                                    responseHandler.errors = List.of(excep.getMessage());
                                    resp.getWriter().write(objectMapper.writeValueAsString(responseHandler));
                                })
//                                .accessDeniedHandler((req, resp, excep) -> {
//                                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                                    resp.setContentType("application/json");
//                                    ResponseHandlerException responseHandlerException = new ResponseHandlerException();
//                                    responseHandlerException.status = HttpServletResponse.SC_FORBIDDEN;
//                                    responseHandlerException.errors = List.of(excep.getMessage());
//                                    resp.getWriter().write(objectMapper.writeValueAsString(responseHandlerException));
//                                })
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
