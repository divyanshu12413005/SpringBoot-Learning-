package com.divyanshu.youtube.hospitalManagement.controller;

import com.divyanshu.youtube.hospitalManagement.dto.DoctorResponseDto;
import com.divyanshu.youtube.hospitalManagement.entity.Patient; // Import Patient
import com.divyanshu.youtube.hospitalManagement.service.PatientService; // Import PatientService
import com.divyanshu.youtube.hospitalManagement.service.DoctorService; // Keep DoctorService if needed for other methods
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin") // Changed to /api/v1/admin to match SecurityConfig
@RequiredArgsConstructor
public class AdminController {

    private final DoctorService doctorService; // Keep if needed
    private final PatientService patientService; // Inject PatientService

    // This is the admin-specific endpoint for patients
    @GetMapping("/patients") // This will map to /api/v1/admin/patients
    public ResponseEntity<List<Patient>> getAdminPatients() {
        List<Patient> patients = patientService.getAllPatients(); // Admin can see all patients
        return ResponseEntity.ok(patients);
    }

    // Existing method, if any, now mapped under /api/v1/admin/doctors
    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponseDto>> getAllDoctors() { // Assuming Doctor is imported
        // Note: This endpoint is now /api/v1/admin/doctors, which also requires ADMIN role
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
}