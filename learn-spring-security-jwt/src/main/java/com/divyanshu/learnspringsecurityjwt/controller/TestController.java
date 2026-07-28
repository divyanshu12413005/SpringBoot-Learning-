package com.divyanshu.learnspringsecurityjwt.controller;

import com.divyanshu.learnspringsecurityjwt.service.RedisService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController {

    private final RedisService redisService;

    public TestController(RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "JWT Authentication Working";
    }

    @PostMapping("/save")
    public String save() {

        redisService.save("course", "Spring Boot Redis");

        return "Saved Successfully";
    }

    @GetMapping("/get")
    public String get() {

        return redisService.get("course");
    }

    @DeleteMapping("/delete")
    public String delete() {

        redisService.delete("course");

        return "Deleted Successfully";
    }
}