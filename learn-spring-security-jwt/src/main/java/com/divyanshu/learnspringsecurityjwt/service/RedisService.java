package com.divyanshu.learnspringsecurityjwt.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Save value
    public void save(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // Get value
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // Save with expiry (minutes)
    public void saveWithExpiry(String key, String value, long minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
    }

    // Delete key
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // Check key exists
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // Get expiry time
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    // Increment value
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    // Set expiry
    public void setExpiry(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    // Blacklist JWT Token
    public void blacklistToken(String token, long expiryInSeconds) {

        redisTemplate.opsForValue().set(
                "blacklist:" + token,
                "true",
                Duration.ofSeconds(expiryInSeconds)
        );
    }

    // Check if token is blacklisted
    public boolean isTokenBlacklisted(String token) {

        return Boolean.TRUE.equals(
                redisTemplate.hasKey("blacklist:" + token)
        );
    }
}