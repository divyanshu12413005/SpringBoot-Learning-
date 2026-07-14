package com.divyanshu.youtube.hospitalManagement.dto;

import lombok.Data;

@Data
public class OnboardDoctorRequestDto {
    private Long userId;
    private String specialization;
    private String email;

    public String getName() {
        return "";
    }
}