package com.divyanshu.learnspringsecurityjwt.controller;

import com.divyanshu.learnspringsecurityjwt.dto.ProfileResponse;
import com.divyanshu.learnspringsecurityjwt.dto.UpdateProfileRequest;
import com.divyanshu.learnspringsecurityjwt.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public String user() {
        return "Welcome User";
    }

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/users/profile")
    public ProfileResponse updateProfile(
            @Valid @RequestBody UpdateProfileRequest request) {

        return userService.updateProfile(request);
    }
}