package com.divyanshu.youtube.hospitalManagement.service;

import com.divyanshu.youtube.hospitalManagement.mapper.DoctorMapper;
import com.divyanshu.youtube.hospitalManagement.dto.DoctorResponseDto;
import com.divyanshu.youtube.hospitalManagement.dto.OnboardDoctorRequestDto;
import com.divyanshu.youtube.hospitalManagement.entity.Doctor;
import com.divyanshu.youtube.hospitalManagement.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper = DoctorMapper.INSTANCE;

    @Transactional
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Optional<Doctor> getDoctorById(Long id) {
        return doctorRepository.findById(id);
    }

    public List<DoctorResponseDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDoctorResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Doctor updateDoctor(Long id, Doctor updatedDoctor) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Doctor with ID " + id + " not found"));
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setEmail(updatedDoctor.getEmail());
        return doctorRepository.save(existingDoctor);
    }

    @Transactional
    public void deleteDoctor(Long id) {
        doctorRepository.deleteById(id);
    }

    @Transactional
    public DoctorResponseDto onBoardNewDoctor(OnboardDoctorRequestDto onboardDoctorRequestDto) {
        Doctor doctor = doctorMapper.toDoctor(onboardDoctorRequestDto);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDoctorResponseDto(savedDoctor);
    }
}