package com.divyanshu.learnspringsecurityjwt.service;

import com.divyanshu.learnspringsecurityjwt.dto.RoleChangeRequest;
import com.divyanshu.learnspringsecurityjwt.dto.UserResponse;
import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.exception.ResourceNotFoundException;
import com.divyanshu.learnspringsecurityjwt.mapper.UserMapper;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void changeUserRole(String userId, RoleChangeRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRole(request.getRole());

        userRepository.save(user);
    }


    public Page<UserResponse> getAllUsers(
            int page,
            int size,
            String sortBy,
            String direction,
            String name,
            Role role) {

        return userRepository.searchUsers(
                name,
                role,
                page,
                size,
                sortBy,
                direction
        );
    }


}