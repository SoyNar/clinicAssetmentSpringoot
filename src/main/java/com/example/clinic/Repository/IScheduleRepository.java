package com.example.clinic.Repository;

import com.example.clinic.Domain.MedicalSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IScheduleRepository extends JpaRepository<MedicalSchedule,Long> {

}
