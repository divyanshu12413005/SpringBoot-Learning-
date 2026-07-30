package com.divyanshu.learnspringsecurityjwt.repository;

import com.divyanshu.learnspringsecurityjwt.dto.UserResponse;
import com.divyanshu.learnspringsecurityjwt.entity.Role;
import com.divyanshu.learnspringsecurityjwt.mapper.UserMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Sort;
import com.divyanshu.learnspringsecurityjwt.entity.User;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public UserRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<UserResponse> searchUsers(
            String name,
            Role role,
            int page,
            int size,
            String sortBy,
            String direction) {

        Query query = new Query();

        if (name != null && !name.isBlank()) {
            query.addCriteria(
                    Criteria.where("name").regex(name, "i")
            );
        }

        if (role != null) {
            query.addCriteria(
                    Criteria.where("role").is(role)
            );
        }

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        query.with(sort);

        Pageable pageable = PageRequest.of(page, size);

// Count without pagination
        long total = mongoTemplate.count(query, User.class);

// Apply pagination
        query.with(pageable);

// Fetch paginated data
        List<User> users = mongoTemplate.find(query, User.class);

        List<UserResponse> response = users.stream()
                .map(UserMapper::toResponse)
                .toList();

        return new PageImpl<>(response, pageable, total);
    }
}