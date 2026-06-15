package com.divyanshu.youtube.hospitalManagement.service;

import com.divyanshu.youtube.hospitalManagement.entity.Insurance;
import com.divyanshu.youtube.hospitalManagement.repository.InsuranceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InsuranceService {

    private final InsuranceRepository insuranceRepository;

    @Transactional
    public Insurance saveInsurance(Insurance insurance) {
        return insuranceRepository.save(insurance);
    }

    public Optional<Insurance> getInsuranceById(Long id) {
        return insuranceRepository.findById(id);
    }

    public List<Insurance> getAllInsurances() {
        return insuranceRepository.findAll();
    }

    @Transactional
    public Insurance updateInsurance(Long id, Insurance updatedInsurance) {
        Insurance existingInsurance = insuranceRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Insurance with ID " + id + " not found"));
        existingInsurance.setPolicyNumber(updatedInsurance.getPolicyNumber());
        existingInsurance.setProvider(updatedInsurance.getProvider());
        existingInsurance.setValidUntil(updatedInsurance.getValidUntil());
        // createdAt is @CreationTimestamp, so it's not updated here
        return insuranceRepository.save(existingInsurance);
    }

    @Transactional
    public void deleteInsurance(Long id) {
        insuranceRepository.deleteById(id);
    }
}