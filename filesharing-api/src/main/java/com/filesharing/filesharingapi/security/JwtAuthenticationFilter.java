package com.filesharing.filesharingapi.security;

import java.io.IOException;

import com.filesharing.filesharingapi.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import com.filesharing.filesharingapi.service.UserServiceImpl;


@Component
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserServiceImpl userService;

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if (isAuthHeaderInvalid(authHeader)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = extractJwtFromHeader(authHeader);
        log.debug("Processing JWT - {}", jwt);

        String userName = jwtService.extractUserName(jwt);
        if (canProceedWithAuthentication(userName)) {
            UserDetails userDetails = loadUserDetails(userName);
            if (userDetails != null && jwtService.isTokenValid(jwt, userDetails)) {
                log.debug("Authenticated user - {}", userName);
                authenticateUser(request, userDetails);
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthHeaderInvalid(String authHeader) {
        return StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, "Bearer ");
    }

    private String extractJwtFromHeader(String authHeader) {
        return authHeader.substring(7); // Extract JWT token after "Bearer "
    }

    private boolean canProceedWithAuthentication(String userName) {
        return StringUtils.isNotEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private UserDetails loadUserDetails(String userName) {
        try {
            return userService.userDetailsService().loadUserByUsername(userName);
        } catch (EntityNotFoundException e) {
            log.error("User not found - {}", userName, e);
            return null;
        }
    }

    private void authenticateUser(HttpServletRequest request, UserDetails userDetails) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        context.setAuthentication(authToken);
        SecurityContextHolder.setContext(context);
    }
}








