package com.lcwd.electronicStore.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    private Logger logger =
            LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String requestHeader = request.getHeader("Authorization");

        logger.info("Authorization Header: {}", requestHeader);

        String username = null;
        String token = null;

        if (requestHeader != null && requestHeader.startsWith("Bearer ")) {

            token = requestHeader.substring(7);

            logger.info("JWT Token received");
            logger.info("JWT Token length: {}", token.length());

            try {

                username = jwtHelper.getUsernameFromToken(token);

                logger.info("Username from JWT: {}", username);

            } catch (ExpiredJwtException e) {

                logger.error("JWT TOKEN EXPIRED", e);

            } catch (MalformedJwtException e) {

                logger.error("MALFORMED JWT TOKEN", e);

            } catch (Exception e) {

                logger.error("JWT ERROR: {}", e.getMessage(), e);
            }

        } else {

            logger.warn("Authorization header is missing or invalid");
        }

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            try {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(username);

                logger.info(
                        "User loaded: {}",
                        userDetails.getUsername()
                );

                logger.info(
                        "User authorities: {}",
                        userDetails.getAuthorities()
                );

                Boolean valid =
                        jwtHelper.validateToken(token, userDetails);

                logger.info("JWT validation result: {}", valid);

                if (valid) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);

                    logger.info(
                            "Authentication successfully set for: {}",
                            username
                    );

                } else {

                    logger.warn("JWT validation FAILED");

                }

            } catch (Exception e) {

                logger.error(
                        "Error while loading user or validating JWT",
                        e
                );
            }
        }

        filterChain.doFilter(request, response);
    }
}
