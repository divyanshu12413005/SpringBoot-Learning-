package com.divyanshu.youtube.hospitalManagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;

    @Column(length = 500)
    private String reason;

    @Column(nullable = false) // Added status field
    private String status; // e.g., "SCHEDULED", "COMPLETED", "CANCELLED"

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)  //Patient is required and not nullable
    private Patient patient;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Doctor doctor;
}