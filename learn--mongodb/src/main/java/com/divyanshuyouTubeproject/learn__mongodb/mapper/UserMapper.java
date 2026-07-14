package com.divyanshuyouTubeproject.learn__mongodb.mapper;

import com.divyanshuyouTubeproject.learn__mongodb.dto.UserRequest;
import com.divyanshuyouTubeproject.learn__mongodb.dto.UserResponse;
import com.divyanshuyouTubeproject.learn__mongodb.enity.User;

public class UserMapper {
    public static User toEntity(UserRequest request){

        User user = new User();

        user.setName(request.getName());
        user.setAge(request.getAge());
        user.setEmail(request.getEmail());

        return user;
    }

    public static UserResponse toResponse(User user){

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .email(user.getEmail())
                .build();
    }
}
