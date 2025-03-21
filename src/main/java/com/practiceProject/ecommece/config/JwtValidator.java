package com.practiceProject.ecommece.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class JwtValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT from the request header
        String Jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (Jwt != null) { // If JWT is present

            // Remove "Bearer " prefix to get the actual token
            Jwt = Jwt.substring(7);

            try {
                // Generate the secret key using the stored key bytes
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

                // Parse the JWT token and extract claims (payload)
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(Jwt)
                        .getBody();

                // Extract user details from the token
                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                // Convert the authorities string into a list of granted authorities
                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                // Create authentication object with the extracted email and roles
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);

                // Set authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // If token parsing fails, throw an exception
                throw new BadCredentialsException("Invalid Token: From JWT Validator..");
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
