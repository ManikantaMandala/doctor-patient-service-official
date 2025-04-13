package com.hcltech.doctor_patient_service.filter;

import com.hcltech.doctor_patient_service.service.JpaUserDetailsService;
import com.hcltech.doctor_patient_service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthRequestFilter extends OncePerRequestFilter {
    private static final Logger jwtLogger = LoggerFactory.getLogger(JwtAuthRequestFilter.class);

    private final JpaUserDetailsService jpaUserDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthRequestFilter(JpaUserDetailsService jpaUserDetailsService, JwtUtil jwtUtil) {
        this.jpaUserDetailsService = jpaUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain chain) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = jwtUtil.extractUsernameFromToken(jwt);
            } catch (Exception e) {
                jwtLogger.error("Error extracting username from token: {}", jwt, e);
            }
            if (username != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {
                try {
                    UserDetails userDetails = this.jpaUserDetailsService.loadUserByUsername(username);

                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        jwtLogger.info("jwt validate successfully");
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext()
                                .setAuthentication(authenticationToken);
                        jwtLogger.info("User authenticated: {}", username);
                    }
                    else {
                        jwtLogger.warn("Invalid JWT token for user: {}", username);
                    }
                } catch (Exception e) {
                    jwtLogger.error("Error during authentication process for user: {}", username, e);
                }
            }
        } else {
            jwtLogger.debug("Authorization header not found or does not start with Bearer.");
            jwtLogger.trace("passing the Authorization object to next filter");
        }
        chain.doFilter(request, response);
    }
}
