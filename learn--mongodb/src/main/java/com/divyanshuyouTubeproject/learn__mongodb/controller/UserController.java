package com.divyanshuyouTubeproject.learn__mongodb.controller;

import com.divyanshuyouTubeproject.learn__mongodb.dto.UserRequest;
import com.divyanshuyouTubeproject.learn__mongodb.dto.UserResponse;
import com.divyanshuyouTubeproject.learn__mongodb.enity.User;
import com.divyanshuyouTubeproject.learn__mongodb.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserRequest request) {

        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<UserResponse> saveUser(
            @Valid @RequestBody UserRequest request) {

        UserResponse response = userService.saveUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @GetMapping("/name/{name}")
    public List<User> getUserByName(@PathVariable String name){
        return userService.getUserByName(name);
    }

    @GetMapping("/age/{age}")
    public List<User> getUsersAboveAge(@PathVariable int age){
        return userService.getUsersAboveAge(age);
    }

    @GetMapping("/search/{name}")
    public List<User> searchUser(@PathVariable String name){
        return userService.searchUser(name);
    }

    @GetMapping("/page")
    public Page<User> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "name") String sortBy) {

        return userService.getUsers(page, size, sortBy);
    }
}