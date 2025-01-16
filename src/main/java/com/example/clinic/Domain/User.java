package com.example.clinic.Domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String fullname;
    private String lastname;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false,unique = true)
    private String password;
//    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isDoctor;


    @OneToMany(mappedBy = "doctor")
    private List<MedicalAppointment> appointmentsDoctor;

    @OneToMany(mappedBy = "patient")
    private List<MedicalAppointment> appointmentsPatient;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL)
    private MedicalSchedule medicalSchedules;

}
