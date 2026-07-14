package com.divyanshuyouTubeproject.learn__mongodb.service;

import com.divyanshuyouTubeproject.learn__mongodb.dto.UserRequest;
import com.divyanshuyouTubeproject.learn__mongodb.dto.UserResponse;
import com.divyanshuyouTubeproject.learn__mongodb.enity.User;
import com.divyanshuyouTubeproject.learn__mongodb.mapper.UserMapper;
import com.divyanshuyouTubeproject.learn__mongodb.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse saveUser(UserRequest request) {

        User user = UserMapper.toEntity(request);

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }
    public UserResponse getUserById(String id) {

        User user = userRepository.findById(id).orElse(null);

        if (user == null) {
            return null;
        }

        return UserMapper.toResponse(user);
    }

    public UserResponse updateUser(String id, UserRequest request) {

        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser == null) {
            return null;
        }

        existingUser.setName(request.getName());
        existingUser.setAge(request.getAge());
        existingUser.setEmail(request.getEmail());

        User updatedUser = userRepository.save(existingUser);

        return UserMapper.toResponse(updatedUser);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }


    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getUserByName(String name){
        return userRepository.findByName(name);
    }

    public List<User> getUsersAboveAge(int age){
        return userRepository.findByAgeGreaterThan(age);
    }

    public List<User> searchUser(String name){
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    public Page<User> getUsers(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, sortBy)
        );

        return userRepository.findAll(pageable);
    }
}