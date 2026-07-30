package com.divyanshu.learnspringsecurityjwt.dto;

import com.divyanshu.learnspringsecurityjwt.entity.Role;
import lombok.Data;

import java.time.Instant;

@Data
public class UserResponse {

    private String id;

    private String name;

    private String email;

    private Role role;

    private Instant createdAt;

    private Instant updatedAt;
}