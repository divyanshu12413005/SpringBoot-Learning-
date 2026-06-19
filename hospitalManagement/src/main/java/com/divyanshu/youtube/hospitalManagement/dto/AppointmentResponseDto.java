package com.divyanshu.youtube.hospitalManagement.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentResponseDto {
    private Long id;
    private LocalDateTime appointmentTime;
    private String reason;
    private String status;
    private Long patientId;
    private Long doctorId;
}