package com.divyanshu.youtube.hospitalManagement.repository;

import com.divyanshu.youtube.hospitalManagement.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Import Optional

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    // Find a doctor whose ID is not the given doctorId
    Optional<Doctor> findFirstByIdIsNot(Long doctorId);
}