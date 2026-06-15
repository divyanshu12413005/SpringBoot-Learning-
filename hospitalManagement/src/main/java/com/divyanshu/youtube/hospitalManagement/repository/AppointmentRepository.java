package com.divyanshu.youtube.hospitalManagement.repository;

import com.divyanshu.youtube.hospitalManagement.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}