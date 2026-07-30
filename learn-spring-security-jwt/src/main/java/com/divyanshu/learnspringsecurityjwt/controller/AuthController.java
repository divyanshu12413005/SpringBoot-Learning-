package com.divyanshu.learnspringsecurityjwt.controller;

import com.divyanshu.learnspringsecurityjwt.dto.*;
import com.divyanshu.learnspringsecurityjwt.service.UserService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.divyanshu.learnspringsecurityjwt.dto.ForgotPasswordRequest;
import com.divyanshu.learnspringsecurityjwt.dto.VerifyOtpRequest;
import com.divyanshu.learnspringsecurityjwt.dto.ResetPasswordRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import com.divyanshu.learnspringsecurityjwt.dto.ApiResponse;
import com.divyanshu.learnspringsecurityjwt.service.RateLimitService;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final RateLimitService rateLimitService;

    public AuthController(
            UserService userService,
            RateLimitService rateLimitService) {

        this.userService = userService;
        this.rateLimitService = rateLimitService;
    }

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        UserResponse response = userService.register(request);

        return new ApiResponse<>(
                true,
                "User registered successfully",
                response
        );
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        String clientIp = httpRequest.getRemoteAddr();

        Bucket loginBucket =
                rateLimitService.resolveBucket(clientIp);

        if (!loginBucket.tryConsume(1)) {

            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(
                            new ApiResponse<>(
                                    false,
                                    "Too many login attempts. Please try again after 1 minute.",
                                    null
                            )
                    );
        }

        LoginResponse response = userService.login(request);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Login successful",
                        response
                )
        );
    }

    @GetMapping("/me")
    public ProfileResponse me() {
        return userService.getCurrentUser();
    }

    @PostMapping("/change-password")
    public ApiResponse<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        userService.changePassword(request);

        return new ApiResponse<>(
                true,
                "Password changed successfully",
                null
        );
    }

    @PostMapping("/refresh")
    public LoginResponse refreshToken(@RequestBody RefreshRequest request) {

        return userService.refreshToken(request);

    }

    @PostMapping("/forgot-password")
    public String forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        return userService.forgotPassword(request);
    }
    @PostMapping("/verify-otp")
    public String verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        return userService.verifyOtp(request);
    }
    @PostMapping("/reset-password")
    public String resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        return userService.resetPassword(request);
    }

    @GetMapping("/google/success")
    public String googleSuccess(Authentication authentication) {

        OAuth2User user = (OAuth2User) authentication.getPrincipal();

        return "Welcome " + user.getAttribute("name")
                + "\nEmail : " + user.getAttribute("email");
    }


    @PutMapping("/profile")
    public ProfileResponse updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        return userService.updateProfile(request);
    }

    @PostMapping("/logout")
    public ApiResponse<String> logout(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Token is missing");
        }

        String token = authHeader.substring(7);

        userService.logout(token);

        return new ApiResponse<>(
                true,
                "Logged out successfully",
                null
        );
    }

}