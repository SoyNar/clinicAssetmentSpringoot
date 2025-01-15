package com.example.clinic.Repository;

import com.example.clinic.Domain.MedicalAppointment;
import com.example.clinic.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IAppointmentRepository extends JpaRepository<MedicalAppointment,Long> {
    List<MedicalAppointment> findByDoctorAndDateTimeBetween(User doctor, LocalDateTime start, LocalDateTime end);

}
