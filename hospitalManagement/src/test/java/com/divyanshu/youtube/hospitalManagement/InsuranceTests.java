package com.divyanshu.youtube.hospitalManagement;

import com.divyanshu.youtube.hospitalManagement.entity.Insurance;
import com.divyanshu.youtube.hospitalManagement.entity.Patient;
import com.divyanshu.youtube.hospitalManagement.entity.type.BloodGroupType;
import com.divyanshu.youtube.hospitalManagement.repository.PatientRepository;
import com.divyanshu.youtube.hospitalManagement.service.InsuranceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class InsuranceTests {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    @Transactional
    void testSaveAndRetrieveInsuranceForPatientId1() { // Renamed method for clarity
        // 1. Fetch Patient with ID 1
        Patient patient = patientRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("Patient with ID 1 not found. Please ensure data.sql or a previous test creates it."));

        // 2. Create an Insurance
        Insurance insurance = Insurance.builder()
                .policyNumber("POL_ID1_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"))) // Unique policy number
                .provider("Test Insurance Co. for ID 1")
                .validUntil(LocalDate.now().plusYears(1))
                .build();

        // 3. Associate Insurance with Patient (owning side is Patient)
        patient.setInsurance(insurance);

        // 4. Save the Patient (which cascades and saves Insurance)
        Patient savedPatient = patientRepository.save(patient);
        assertNotNull(savedPatient.getId());
        assertNotNull(savedPatient.getInsurance());
        assertNotNull(savedPatient.getInsurance().getId());

        // 5. Retrieve the Insurance using its ID
        Optional<Insurance> retrievedInsuranceOptional = insuranceService.getInsuranceById(savedPatient.getInsurance().getId());
        assertTrue(retrievedInsuranceOptional.isPresent());
        Insurance retrievedInsurance = retrievedInsuranceOptional.get();

        // 6. Verify details
        assertEquals(insurance.getPolicyNumber(), retrievedInsurance.getPolicyNumber());
        assertEquals(insurance.getProvider(), retrievedInsurance.getProvider());
        assertEquals(insurance.getValidUntil(), retrievedInsurance.getValidUntil());
        assertNotNull(retrievedInsurance.getCreatedAt());

        // Verify bidirectional link from Insurance to Patient
        assertNotNull(retrievedInsurance.getPatient());
        assertEquals(savedPatient.getId(), retrievedInsurance.getPatient().getId());

        System.out.println("Saved Patient with Insurance: " + savedPatient);
        System.out.println("Retrieved Insurance: " + retrievedInsurance);
    }
}