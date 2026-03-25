package com.example.backendapi.security;

import com.example.backendapi.domain.enums.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class HeaderAuthFilter extends OncePerRequestFilter {

    public static final String HEADER_USER = "X-USER";
    public static final String HEADER_ROLE = "X-ROLE";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String username = request.getHeader(HEADER_USER);
        String roleHeader = request.getHeader(HEADER_ROLE);

        if (username != null && !username.isBlank()) {
            UserRole role = UserRole.USER;
            if (roleHeader != null && !roleHeader.isBlank()) {
                try {
                    role = UserRole.valueOf(roleHeader.trim().toUpperCase());
                } catch (Exception ignored) {
                    // Default to USER
                }
            }

            AppUserPrincipal principal = new AppUserPrincipal(username, role);
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, "N/A", principal.authorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }
}
