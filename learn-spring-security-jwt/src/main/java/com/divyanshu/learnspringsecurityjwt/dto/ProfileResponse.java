package com.divyanshu.learnspringsecurityjwt.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class ProfileResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}