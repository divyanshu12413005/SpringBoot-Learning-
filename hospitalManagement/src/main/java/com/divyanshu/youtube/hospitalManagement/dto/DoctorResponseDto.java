package com.divyanshu.youtube.hospitalManagement.dto;

import lombok.Data;

@Data
public class DoctorResponseDto {
    private long id;
    private String name;
    private String specialization;
    private String email;
}