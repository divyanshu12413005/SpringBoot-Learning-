package com.divyanshu.learnspringsecurityjwt.dto;

import lombok.Data;

@Data
public class RefreshRequest {

    private String refreshToken;

}