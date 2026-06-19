package com.divyanshu.youtube.hospitalManagement.service;

import com.divyanshu.youtube.hospitalManagement.mapper.PatientMapper;
import com.divyanshu.youtube.hospitalManagement.dto.PatientResponseDto;
import com.divyanshu.youtube.hospitalManagement.entity.Patient;
import com.divyanshu.youtube.hospitalManagement.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper = PatientMapper.INSTANCE;

    @Transactional
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id).orElseThrow();
    }

    public List<PatientResponseDto> getAllPatients(Integer pageNumber, Integer pageSize) {
        return patientRepository.findAll(PageRequest.of(pageNumber, pageSize)).stream()
                .map(patientMapper::toPatientResponseDto)
                .collect(Collectors.toList());
    }

    public PatientResponseDto getPatientByName(String name) {
        Patient patient = patientRepository.findByName(name).orElseThrow();
        return patientMapper.toPatientResponseDto(patient);
    }

    public List<Patient> getAllPatients() {
        return List.of();
    }
}