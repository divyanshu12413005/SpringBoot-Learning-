package com.divyanshu.youtube.hospitalManagement.dto;

import lombok.Data;

@Data
public class OnboardDoctorRequestDto {
    private String name;
    private String specialization;
    private String email;
}