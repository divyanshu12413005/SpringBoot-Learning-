package com.divyanshuyouTubeproject.learn__mongodb.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 1, message = "Age must be greater than 0")
    private int age;

    @Email(message = "Invalid Email")
    private String email;
}