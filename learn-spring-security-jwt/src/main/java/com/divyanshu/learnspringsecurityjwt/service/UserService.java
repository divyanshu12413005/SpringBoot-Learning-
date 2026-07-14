package com.divyanshu.learnspringsecurityjwt.service;

import com.divyanshu.learnspringsecurityjwt.dto.RegisterRequest;
import com.divyanshu.learnspringsecurityjwt.dto.UserResponse;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.mapper.UserMapper;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse register(RegisterRequest request){

        User user = UserMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setRole("USER");

        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);

    }

}