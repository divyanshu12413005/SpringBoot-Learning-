package com.divyanshu.youtube.hospitalManagement.entity;

import com.divyanshu.youtube.hospitalManagement.entity.type.BloodGroupType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Getter
@Setter
@Table(name="patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    private String gender;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "blood_group", nullable = false)
    @Enumerated(EnumType.STRING)
    private BloodGroupType bloodGroup;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToOne(cascade={CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name="patient_insurance_id")
    private  Insurance insurance;


    @OneToMany(mappedBy = "patient", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
    @ToString.Exclude
    private List<Appointment>appointments=new ArrayList<>();
}