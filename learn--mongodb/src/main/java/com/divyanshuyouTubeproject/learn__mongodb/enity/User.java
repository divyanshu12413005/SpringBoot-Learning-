package com.divyanshuyouTubeproject.learn__mongodb.enity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    @Min(value = 1, message = "Age should be greater than 0")
    @Max(value = 120, message = "Age should be less than 120")
    private int age;

    @Email(message = "Invalid Email")
    @NotBlank(message = "Email is required")
    private String email;
}