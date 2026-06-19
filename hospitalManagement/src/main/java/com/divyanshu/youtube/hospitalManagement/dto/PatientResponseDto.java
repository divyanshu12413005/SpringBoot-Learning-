package com.divyanshu.youtube.hospitalManagement.dto;

import com.divyanshu.youtube.hospitalManagement.entity.type.BloodGroupType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientResponseDto {
    private long id;
    private String name;
    private String gender;
    private String email;
    private BloodGroupType bloodGroup;
    private LocalDate birthDate;
}