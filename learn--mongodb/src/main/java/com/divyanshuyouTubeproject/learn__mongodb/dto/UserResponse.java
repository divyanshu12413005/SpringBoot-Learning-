package com.divyanshuyouTubeproject.learn__mongodb.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String id;
    private String name;
    private int age;
    private String email;
}
