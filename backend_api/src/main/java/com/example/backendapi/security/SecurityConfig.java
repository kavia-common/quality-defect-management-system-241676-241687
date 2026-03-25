package com.example.backendapi.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${app.security.enabled:true}")
    private boolean securityEnabled;

    @Value("${app.cors.allowed-origins:http://localhost:3000}")
    private String allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Stateless simple auth; no sessions and no CSRF (API use).
        http.csrf(csrf -> csrf.disable());

        // CORS (also configured via WebMvcConfigurer below); keep here for Security chain.
        http.cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration cfg = new CorsConfiguration();
            cfg.setAllowedOrigins(List.of(allowedOrigins.split(",")));
            cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
            cfg.setAllowedHeaders(List.of("*"));
            cfg.setAllowCredentials(true);
            return cfg;
        }));

        http.httpBasic(Customizer.withDefaults());
        http.formLogin(form -> form.disable());

        if (securityEnabled) {
            http.authorizeHttpRequests(auth -> auth
                    // public
                    .requestMatchers("/", "/docs", "/swagger-ui.html", "/swagger-ui/**", "/api-docs/**", "/api/info", "/health", "/actuator/**").permitAll()
                    // users restricted
                    .requestMatchers("/api/users/**").hasRole("ADMIN")
                    // export should be at least MANAGER
                    .requestMatchers(HttpMethod.GET, "/api/export/**").hasAnyRole("ADMIN", "MANAGER")
                    // everything else authenticated (USER ok)
                    .anyRequest().authenticated()
            );
            http.addFilterBefore(new HeaderAuthFilter(), UsernamePasswordAuthenticationFilter.class);
        } else {
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        }

        return http.build();
    }
}
