package com.divyanshu.learnspringsecurityjwt.mapper;

import com.divyanshu.learnspringsecurityjwt.dto.RegisterRequest;
import com.divyanshu.learnspringsecurityjwt.dto.UserResponse;
import com.divyanshu.learnspringsecurityjwt.entity.User;

public class UserMapper {

    public static User toEntity(RegisterRequest request){

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return user;
    }

    public static UserResponse toResponse(User user){

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}