package com.example.clinic.Domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="medical_appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalAppointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
//    private LocalTime startTime;
//    @Column(nullable = false)
//    private LocalTime endTime;
//    @Column(nullable = false)

    private String reason;
    private String medications;
    private String diagnostics;
    private String notes;

    private  LocalDateTime dateTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private User patient;

    @ManyToOne
    private User doctor;
}
