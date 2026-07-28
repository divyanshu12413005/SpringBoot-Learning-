package com.divyanshu.learnspringsecurityjwt.service;

import com.divyanshu.learnspringsecurityjwt.dto.*;
import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.mapper.UserMapper;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;

import org.springframework.cache.annotation.CachePut;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.divyanshu.learnspringsecurityjwt.dto.UpdateProfileRequest;
import com.divyanshu.learnspringsecurityjwt.dto.ChangePasswordRequest;
import com.divyanshu.learnspringsecurityjwt.dto.ForgotPasswordRequest;


import com.divyanshu.learnspringsecurityjwt.dto.VerifyOtpRequest;
import com.divyanshu.learnspringsecurityjwt.dto.ResetPasswordRequest;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    private final OtpService otpService;
    private final EmailService emailService;

    private final RedisService redisService;

    private final CachedUserService cachedUserService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       OtpService otpService,
                       EmailService emailService,
                       RedisService redisService,
                       CachedUserService cachedUserService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.otpService = otpService;
        this.emailService = emailService;
        this.redisService = redisService;
        this.cachedUserService = cachedUserService;
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

        return cachedUserService.getUser(email);
    }


    public ProfileResponse updateProfile(UpdateProfileRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(request.getName());

        User updatedUser = userRepository.save(user);

        return cachedUserService.updateCache(updatedUser);
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

    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));


        String rateLimitKey = "otp-limit:" + user.getEmail();

        Long count = redisService.increment(rateLimitKey);

        if (count == 1) {
            redisService.setExpiry(rateLimitKey, 10, TimeUnit.MINUTES);
        }

        if (count > 3) {
            throw new RuntimeException(
                    "Too many OTP requests. Please try again after 10 minutes."
            );
        }

        String otpCode = otpService.generateOtp();

        redisService.saveWithExpiry(
                "otp:" + user.getEmail(),
                otpCode,
                5
        );

        emailService.sendOtp(user.getEmail(), otpCode);


        return "OTP sent successfully";

    }

    public String verifyOtp(VerifyOtpRequest request) {

        String key = "otp:" + request.getEmail();

        String savedOtp = redisService.get(key);

        if (savedOtp == null) {
            throw new RuntimeException("OTP Expired");
        }

        if (!savedOtp.equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        redisService.saveWithExpiry(
                "verified:" + request.getEmail(),
                "true",
                5
        );

        return "OTP Verified Successfully";
    }
    public String resetPassword(ResetPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String verified = redisService.get("verified:" + request.getEmail());

        if (verified == null || !verified.equals("true")) {
            throw new RuntimeException("OTP not verified");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);

        redisService.delete("otp:" + request.getEmail());
        redisService.delete("verified:" + request.getEmail());

        return "Password Reset Successfully";
    }




}