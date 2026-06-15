package com.divyanshu.youtube.hospitalManagement.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp; // Import CreationTimestamp

import java.time.LocalDate; // Import LocalDate
import java.time.LocalDateTime; // Import LocalDateTime

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Insurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String policyNumber;

    @Column(nullable = false)
    private String provider;

    @Column
    private LocalDate validUntil;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(mappedBy = "insurance")  //inverse side of the relationship
    private Patient patient;
}