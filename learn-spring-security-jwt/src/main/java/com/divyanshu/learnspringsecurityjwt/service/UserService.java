package com.divyanshu.learnspringsecurityjwt.service;

import com.divyanshu.learnspringsecurityjwt.dto.*;
import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.mapper.UserMapper;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.divyanshu.learnspringsecurityjwt.dto.UpdateProfileRequest;
import com.divyanshu.learnspringsecurityjwt.dto.ChangePasswordRequest;

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

        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        user.setRefreshToken(refreshToken);

        user.setRefreshTokenExpiry(LocalDateTime.now().plusDays(7));

        userRepository.save(user);

        return new LoginResponse(accessToken, refreshToken);
    }

    public LoginResponse refreshToken(RefreshRequest request) {

        User user = userRepository.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid Refresh Token"));

        if (user.getRefreshTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Refresh Token Expired");
        }

        String newAccessToken = jwtService.generateToken(user.getEmail());

        return new LoginResponse(newAccessToken, user.getRefreshToken());
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

    public ProfileResponse getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toProfileResponse(user);
    }

    public ProfileResponse updateProfile(UpdateProfileRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());

        User updatedUser = userRepository.save(user);

        return UserMapper.toProfileResponse(updatedUser);
    }

    public String changePassword(ChangePasswordRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        return "Password changed successfully";
    }

}