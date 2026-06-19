package com.divyanshu.youtube.hospitalManagement.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails divyanshuUser = User.withUsername("divyanshu")
                .password(passwordEncoder.encode("pass"))
                .roles("USER", "ADMIN") // Assign roles
                .build();

        UserDetails patientUser = User.withUsername("patientuser")
                .password(passwordEncoder.encode("pass"))
                .roles("USER") // Only USER role
                .build();

        return new InMemoryUserDetailsManager(divyanshuUser, patientUser);
    }
}