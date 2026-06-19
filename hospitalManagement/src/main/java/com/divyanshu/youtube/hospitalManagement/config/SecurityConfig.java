package com.divyanshu.youtube.hospitalManagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
      
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for simplicity in API testing, enable in production
            .authorizeHttpRequests(authorize -> authorize
                // Publicly accessible endpoints
                .requestMatchers("/login", "/v1/logout", "/api/v1/public/**").permitAll() // Updated to cover all public endpoints

                // Admin-only access
                .requestMatchers("/api/v1/admin/patients").hasRole("ADMIN")
                .requestMatchers("/api/admin/**", "/api/doctors/**").hasRole("ADMIN") // Existing admin-only rules

                // User or Admin access
                .requestMatchers("/api/patients/**", "/api/appointments/**").hasAnyRole("USER", "ADMIN")

                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            .formLogin(org.springframework.security.config.Customizer.withDefaults()) // Enable default Form Login
            .logout(logout -> logout
                .logoutUrl("/v1/logout") // Custom logout URL
                .invalidateHttpSession(true) // Invalidate HTTP session on logout
                .deleteCookies("JSESSIONID") // Delete session cookie
            );
        return http.build();
    }
}