package com.divyanshuyouTubeproject.learn__mongodb.repository;

import com.divyanshuyouTubeproject.learn__mongodb.enity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByEmail(String email);

    List<User> findByName(String name);

    List<User> findByAgeGreaterThan(int age);

    List<User> findByNameContainingIgnoreCase(String name);
}