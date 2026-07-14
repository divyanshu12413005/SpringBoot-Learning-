package com.divyanshu.learnspringsecurityjwt.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    private String provider;

    private String profilePicture;


    @Field("role")
    private Role role;

    private LocalDateTime createdAt;

    private String refreshToken;

    private LocalDateTime refreshTokenExpiry;
}