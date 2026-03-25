package com.example.backendapi.security;

import com.example.backendapi.domain.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * Simple principal built from request headers (X-USER, X-ROLE).
 * This is intentionally lightweight for a template; can be replaced with JWT/OAuth later.
 */
public record AppUserPrincipal(String username, UserRole role) {

    public Collection<? extends GrantedAuthority> authorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
