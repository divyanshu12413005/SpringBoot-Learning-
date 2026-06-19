package com.divyanshu.youtube.hospitalManagement.service;

import com.divyanshu.youtube.hospitalManagement.mapper.AppointmentMapper;
import com.divyanshu.youtube.hospitalManagement.dto.AppointmentResponseDto;
import com.divyanshu.youtube.hospitalManagement.entity.Appointment;
import com.divyanshu.youtube.hospitalManagement.entity.Doctor;
import com.divyanshu.youtube.hospitalManagement.repository.AppointmentRepository;
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
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentMapper appointmentMapper = AppointmentMapper.INSTANCE;

    @Transactional
    public Appointment saveAppointment(Appointment appointment) {
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

        existingAppointment.setAppointmentTime(updatedAppointment.getAppointmentTime());
        existingAppointment.setReason(updatedAppointment.getReason());
        existingAppointment.setStatus(updatedAppointment.getStatus());

        if (updatedAppointment.getDoctor() != null && updatedAppointment.getDoctor().getId() != 0) {
            Optional<Doctor> specifiedDoctor = doctorRepository.findById(updatedAppointment.getDoctor().getId());

            if (specifiedDoctor.isPresent()) {
                existingAppointment.setDoctor(specifiedDoctor.get());
            } else {
                System.out.println("Specified doctor with ID " + updatedAppointment.getDoctor().getId() + " not found. Attempting to reassign.");
                Doctor alternativeDoctor = doctorRepository.findAll().stream()
                        .findFirst()
                        .orElseThrow(() -> new NoSuchElementException("No alternative doctor found for reassignment."));
                existingAppointment.setDoctor(alternativeDoctor);
                System.out.println("Appointment reassigned to Dr. " + alternativeDoctor.getName() + " (ID: " + alternativeDoctor.getId() + ")");
            }
        }
        return appointmentRepository.save(existingAppointment);
    }

    @Transactional
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public List<AppointmentResponseDto> getAllAppointmentsByDoctorId(long doctorId) {
        return appointmentRepository.findAllByDoctorId(doctorId).stream()
                .map(appointmentMapper::toAppointmentResponseDto)
                .collect(Collectors.toList());
    }
}