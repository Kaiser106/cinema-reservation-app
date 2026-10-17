package com.cinema.infrastructure.auth;

import com.cinema.application.port.SessionAuthPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class SessionAuthenticationFilter extends OncePerRequestFilter {
    private final SessionAuthPort sessionAuthPort;

    public SessionAuthenticationFilter(SessionAuthPort sessionAuthPort) {
        this.sessionAuthPort = sessionAuthPort;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("X-Session-Token");
        if (token != null && !token.isBlank()) {
            sessionAuthPort.findValidSession(token).ifPresent(session -> {
                var principal = new SessionPrincipal(session.userId(), session.role(), token);
                var auth = new UsernamePasswordAuthenticationToken(principal, null, List.of(new SimpleGrantedAuthority("ROLE_" + session.role())));
                SecurityContextHolder.getContext().setAuthentication(auth);
            });
        }
        filterChain.doFilter(request, response);
    }
}
