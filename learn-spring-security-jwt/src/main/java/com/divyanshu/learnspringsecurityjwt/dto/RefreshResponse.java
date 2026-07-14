package com.divyanshu.learnspringsecurityjwt.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshResponse {

    private String accessToken;

}