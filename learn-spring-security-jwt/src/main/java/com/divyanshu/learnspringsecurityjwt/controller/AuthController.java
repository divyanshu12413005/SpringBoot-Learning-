package com.divyanshu.learnspringsecurityjwt.controller;

import com.divyanshu.learnspringsecurityjwt.dto.*;
import com.divyanshu.learnspringsecurityjwt.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterRequest request){

        return userService.register(request);

    }
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        return userService.login(request);

    }

    @GetMapping("/me")
    public ProfileResponse me() {
        return userService.getCurrentUser();
    }

    @PostMapping("/change-password")
    public String changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        return userService.changePassword(request);
    }

    @PostMapping("/refresh")
    public LoginResponse refreshToken(@RequestBody RefreshRequest request) {

        return userService.refreshToken(request);

    }

}