package com.divyanshu.learnspringsecurityjwt.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileResponse {

    private String id;

    private String name;

    private String email;

    private String role;

    private LocalDateTime createdAt;
}