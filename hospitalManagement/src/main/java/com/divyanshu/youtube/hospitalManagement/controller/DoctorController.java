package com.divyanshu.youtube.hospitalManagement.controller;

import com.divyanshu.youtube.hospitalManagement.dto.AppointmentResponseDto;
import com.divyanshu.youtube.hospitalManagement.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctors") // Changed to /api/doctors to match SecurityConfig
@RequiredArgsConstructor
public class DoctorController {

    private final AppointmentService appointmentService;

    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentsOfDoctor() {
        // Hardcoding doctor ID for now, as there is no security context
        long doctorId = 1L;
        return ResponseEntity.ok(appointmentService.getAllAppointmentsByDoctorId(doctorId));
    }

}