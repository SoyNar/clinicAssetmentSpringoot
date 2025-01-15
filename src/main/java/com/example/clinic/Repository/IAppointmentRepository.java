package com.example.clinic.Repository;

import com.example.clinic.Domain.MedicalAppointment;
import com.example.clinic.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IAppointmentRepository extends JpaRepository<MedicalAppointment,Long> {
    List<MedicalAppointment> findByDoctorAndDateTimeBetween(User doctor, LocalDateTime start, LocalDateTime end);
    List<MedicalAppointment> findByPatientId(Long patientId);
    Optional<MedicalAppointment> findById(Long id);


}
