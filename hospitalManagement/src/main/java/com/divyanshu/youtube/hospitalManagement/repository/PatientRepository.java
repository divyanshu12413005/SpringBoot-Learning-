package com.divyanshu.youtube.hospitalManagement.repository;

import com.divyanshu.youtube.hospitalManagement.entity.Patient;
import com.divyanshu.youtube.hospitalManagement.entity.type.BloodGroupType; // Import BloodGroupType
import com.divyanshu.youtube.hospitalManagement.dto.BloodGroupCountResponseEntity; // Import the DTO
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Import Modifying
import org.springframework.data.jpa.repository.Query; // Import Query
import org.springframework.data.repository.query.Param; // Import Param
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional; // Import Transactional

import java.time.LocalDate;
import java.util.List; // Import List
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByName(String name); // Changed to return Optional
    Optional<Patient> findByNameStartingWith(String prefix);
    Optional<Patient> findByBirthDate(LocalDate birthDate);
    List<Patient> findByNameContainingIgnoreCase(String keyword); // Case-insensitive search
    List<Patient> findByGender(String gender);
    List<Patient> findByBloodGroup(BloodGroupType bloodGroup);

    // Custom @Query to find patients by blood group using @Param
    @Query("SELECT p FROM Patient p WHERE p.bloodGroup = :bloodGroup")
    List<Patient> findByBloodGroupWithQuery(@Param("bloodGroup") BloodGroupType bloodGroup);

    // Custom @Query to find patients born after a specific date using @Param
    @Query("SELECT p FROM Patient p WHERE p.birthDate > :birthDate")
    List<Patient> findByBornAfterDate(@Param("birthDate") LocalDate birthDate);

    // Custom @Query to group patients by blood group and count them, returning DTO
    @Query("SELECT NEW com.divyanshu.youtube.hospitalManagement.dto.BloodGroupCountResponseEntity(p.bloodGroup, COUNT(p))" +
            " FROM Patient p GROUP BY p.bloodGroup")
    List<BloodGroupCountResponseEntity> countPatientsByBloodGroup();

    // Native SQL query to find all patients
    @Query(value = "SELECT * FROM patient", nativeQuery = true)
    List<Patient> findAllPatientsNative();

    // Custom @Query to update a patient's name by ID using @Modifying and @Transactional
    @Modifying
    @Transactional
    @Query("UPDATE Patient p SET p.name = :newName WHERE p.id = :id")
    int updatePatientName(@Param("newName") String newName, @Param("id") Long id);

    // Custom @Query with LEFT JOIN FETCH for appointments and doctors
    @Query("SELECT p FROM Patient p LEFT JOIN FETCH p.appointments a LEFT JOIN FETCH a.doctor")
    List<Patient> findAllPatientsWithAppointmentsAndDoctors();
}