package com.divyanshu.youtube.hospitalManagement.service;

import com.divyanshu.youtube.hospitalManagement.entity.Appointment;
import com.divyanshu.youtube.hospitalManagement.entity.Doctor; // Import Doctor
import com.divyanshu.youtube.hospitalManagement.repository.AppointmentRepository;
import com.divyanshu.youtube.hospitalManagement.repository.DoctorRepository; // Import DoctorRepository
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository; // Inject DoctorRepository

    @Transactional
    public Appointment saveAppointment(Appointment appointment) {
        // Ensure the doctor exists before saving the appointment
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == 0) {
            throw new IllegalArgumentException("Doctor must be provided for an appointment.");
        }
        Doctor assignedDoctor = doctorRepository.findById(appointment.getDoctor().getId())
                .orElseThrow(() -> new NoSuchElementException("Doctor with ID " + appointment.getDoctor().getId() + " not found. Cannot save appointment."));
        appointment.setDoctor(assignedDoctor);
        return appointmentRepository.save(appointment);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Transactional
    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Appointment with ID " + id + " not found"));

        // Update basic fields
        existingAppointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        existingAppointment.setReason(updatedAppointment.getReason());
        existingAppointment.setStatus(updatedAppointment.getStatus());

        // Handle doctor reassignment logic
        if (updatedAppointment.getDoctor() != null && updatedAppointment.getDoctor().getId() != 0) {
            Optional<Doctor> specifiedDoctor = doctorRepository.findById(updatedAppointment.getDoctor().getId());

            if (specifiedDoctor.isPresent()) {
                existingAppointment.setDoctor(specifiedDoctor.get());
            } else {
                // Specified doctor not found, try to find an alternative
                System.out.println("Specified doctor with ID " + updatedAppointment.getDoctor().getId() + " not found. Attempting to reassign.");
                Doctor alternativeDoctor = doctorRepository.findAll().stream()
                        .findFirst() // Just pick the first available doctor for simplicity
                        .orElseThrow(() -> new NoSuchElementException("No alternative doctor found for reassignment."));
                existingAppointment.setDoctor(alternativeDoctor);
                System.out.println("Appointment reassigned to Dr. " + alternativeDoctor.getName() + " (ID: " + alternativeDoctor.getId() + ")");
            }
        } else {
            // If no doctor was specified in the update, keep the existing one or handle as needed
            // For now, we'll assume if no doctor is specified, the existing one remains.
            // If the existing doctor was null (which shouldn't happen due to nullable=false on @JoinColumn),
            // you might want to assign an alternative here too.
        }

        return appointmentRepository.save(existingAppointment);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}