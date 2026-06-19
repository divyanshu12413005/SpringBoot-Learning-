package com.divyanshu.youtube.hospitalManagement.controller;

import com.divyanshu.youtube.hospitalManagement.dto.DoctorResponseDto;
import com.divyanshu.youtube.hospitalManagement.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public") // Base URL for public endpoints
@RequiredArgsConstructor
public class PublicController {

    private final DoctorService doctorService;

    @GetMapping("/doctors") // This will map to /api/v1/public/doctors
    public ResponseEntity<List<DoctorResponseDto>> getPublicDoctors() {
        List<DoctorResponseDto> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
}