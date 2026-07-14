package com.divyanshu.learnspringsecurityjwt.repository;

import com.divyanshu.learnspringsecurityjwt.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

}
