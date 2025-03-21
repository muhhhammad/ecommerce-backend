package com.practiceProject.ecommece.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class EcommerceConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Set session management to stateless (JWT-based authentication).
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").authenticated() // Require authentication for all API endpoints.
                        .anyRequest().permitAll() // Allow all other requests (e.g., static files, home page) without authentication.
                )
                .addFilterBefore(new JwtValidator(), BasicAuthenticationFilter.class) // Add JWT validation filter before the default authentication filter.
                .csrf(csrf -> csrf.disable()) // Disable CSRF protection (not needed for JWT-based APIs).
                .cors(cors -> cors.configurationSource(request -> { // Configure CORS settings.
                    CorsConfiguration cfg = new CorsConfiguration();
                    cfg.setAllowedOrigins(List.of(
                            "http://localhost:3000")); // Allow requests from the frontend. // Make Sure Your URl is correct..

                    cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow specific HTTP methods.
                    cfg.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type")); // Allow these headers in requests.
                    cfg.setAllowCredentials(true); // Allow credentials (cookies, authorization headers).
                    cfg.setExposedHeaders(Arrays.asList("Authorization")); // Expose the Authorization header in responses.
                    cfg.setMaxAge(3600L); // Set the maximum age (cache duration) for pre-flight requests.

                    return cfg;
                }));

        return http.build(); // Build and return the security filter chain.
    }


    //Extracting BCrypt to use it as Spring bean in our application....
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

