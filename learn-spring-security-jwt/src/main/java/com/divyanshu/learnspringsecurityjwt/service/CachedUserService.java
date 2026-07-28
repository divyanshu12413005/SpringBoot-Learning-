package com.divyanshu.learnspringsecurityjwt.service;

import com.divyanshu.learnspringsecurityjwt.dto.ProfileResponse;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import com.divyanshu.learnspringsecurityjwt.mapper.UserMapper;
import com.divyanshu.learnspringsecurityjwt.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CachedUserService {

    private final UserRepository userRepository;

    public CachedUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable(value = "users", key = "#email")
    public ProfileResponse getUser(String email) {

        System.out.println("Fetching user from MongoDB...");

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserMapper.toProfileResponse(user);
    }

    @CachePut(value = "users", key = "#result.email")
    public ProfileResponse updateCache(User user) {

        System.out.println("Updating Cache...");

        return UserMapper.toProfileResponse(user);
    }

    @CacheEvict(value = "users", key = "#email")
    public void removeCache(String email) {

        System.out.println("Removing Cache...");
    }
}