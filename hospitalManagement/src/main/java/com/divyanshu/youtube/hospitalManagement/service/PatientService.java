package com.divyanshu.youtube.hospitalManagement.service;

import com.divyanshu.youtube.hospitalManagement.entity.Patient;
import com.divyanshu.youtube.hospitalManagement.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.function.Supplier; // Import Supplier

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;

   @Transactional
    public Patient getPatientById(Long id){
       Supplier<NoSuchElementException> exceptionSupplier = () -> new NoSuchElementException("Patient with ID " + id + " not found");
       return patientRepository.findById(id).orElseThrow(exceptionSupplier);
    }

    public Patient getPatientByName(String name) {
        Supplier<NoSuchElementException> exceptionSupplier = () -> new NoSuchElementException("Patient with name " + name + " not found");
        return patientRepository.findByName(name).orElseThrow(exceptionSupplier);
    }
}