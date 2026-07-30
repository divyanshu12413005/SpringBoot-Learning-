package com.divyanshu.learnspringsecurityjwt.repository;

import com.divyanshu.learnspringsecurityjwt.dto.UserResponse;
import com.divyanshu.learnspringsecurityjwt.entity.Role;
import org.springframework.data.domain.Page;

public interface UserRepositoryCustom {

    Page<UserResponse> searchUsers(
            String name,
            Role role,
            int page,
            int size,
            String sortBy,
            String direction
    );
}