package com.divyanshu.youtube.hospitalManagement;

import com.divyanshu.youtube.hospitalManagement.dto.PatientResponseDto;
import com.divyanshu.youtube.hospitalManagement.entity.Patient;
import com.divyanshu.youtube.hospitalManagement.repository.PatientRepository;
import com.divyanshu.youtube.hospitalManagement.service.PatientService;
import com.divyanshu.youtube.hospitalManagement.entity.type.BloodGroupType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:postgresql://localhost:5432/hospitalDB",
    "spring.datasource.username=postgres",
    "spring.datasource.password=Divyanshu4321", // <-- IMPORTANT: SET YOUR PASSWORD HERE
    "spring.datasource.driver-class-name=org.postgresql.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
    "spring.jpa.hibernate.ddl-auto=update"
})
public class PatientTests {

    @Autowired
    private  PatientRepository patientRepository;
    @Autowired
    private PatientService patientService;

    @Test
    public void testPatientRepository(){
       List<Patient> patientList = patientRepository.findAll();
        System.out.println(patientList);

    }
    
    @Test
    @Transactional
    public void testTransactionMethods(){
        // First save a test patient
        Patient newPatient = new Patient();
        newPatient.setName("Test Patient");
        // Generate a unique email using a timestamp
        String uniqueEmail = "test_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + "@example.com";
        newPatient.setEmail(uniqueEmail);
        newPatient.setGender("Male");
        newPatient.setBloodGroup(BloodGroupType.O_POSITIVE);
        Patient savedPatient = patientRepository.save(newPatient);

        // Then fetch the saved patient by its generated ID
        PatientResponseDto patient = patientService.getPatientByName(newPatient.getName()); // Find by name
        System.out.println(patient);
    }
}