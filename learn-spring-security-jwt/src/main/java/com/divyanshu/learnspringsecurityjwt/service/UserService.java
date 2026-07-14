package com.divyanshu.learnspringsecurityjwt.service;

import com.divyanshu.learnspringsecurityjwt.dto.LoginRequest;
import com.divyanshu.learnspringsecurityjwt.dto.LoginResponse;
import com.divyanshu.learnspringsecurityjwt.dto.RegisterRequest;
import com.divyanshu.learnspringsecurityjwt.dto.UserResponse;
import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.mapper.UserMapper;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        String accessToken = jwtService.generateToken(user.getEmail());

// Abhi temporary refresh token
        String refreshToken = "TEMP_REFRESH_TOKEN";

        return new LoginResponse(accessToken, refreshToken);
    }

    public UserResponse register(RegisterRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = UserMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

}