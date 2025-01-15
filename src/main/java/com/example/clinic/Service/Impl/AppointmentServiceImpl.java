package com.example.clinic.Service.Impl;

import com.example.clinic.Domain.MedicalAppointment;
import com.example.clinic.Domain.MedicalSchedule;
import com.example.clinic.Domain.Role;
import com.example.clinic.Domain.User;
import com.example.clinic.Dto.RequestDto.AppointmentRequestDto;
import com.example.clinic.Dto.ResponseDto.AppointmentResponseDto;
import com.example.clinic.Repository.IAppointmentRepository;
import com.example.clinic.Repository.IUserRepository;
import com.example.clinic.Service.IAppointmentService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentServiceImpl implements IAppointmentService {

    private final IUserRepository userRepository;
    private final IAppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(IUserRepository userRepository, IAppointmentRepository appointmentRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
    }
    @Override
    public List<LocalDateTime> getAvailableSlot(Long doctorId, LocalDate date) {

        //buscar doctor por id
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if(!doctor.getRole().equals(Role.DOCTOR)){
            throw  new IllegalArgumentException("Tiene que se un doctor");
        }
 //verificar si el doctor tiene un horario asignado usando la entidad
        MedicalSchedule schedule = doctor.getMedicalSchedules();
        if (schedule == null) {
            throw new RuntimeException("EL doctor no tiene un horario");
        }

        LocalDateTime startOfDay = date.atTime(schedule.getStartTime());
        LocalDateTime endOfDay = date.atTime(schedule.getEndTime());

        //lista de citas medicas del doctor
        //buscar las citas medicas del doctor
        List<MedicalAppointment> existingAppointments = appointmentRepository
                .findByDoctorAndDateTimeBetween(doctor, startOfDay, endOfDay);
//slots disponibles listado
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalDateTime currentSlot = startOfDay;

        while (currentSlot.plusMinutes(schedule.getDuration()).isBefore(endOfDay) ||
                currentSlot.plusMinutes(schedule.getDuration()).equals(endOfDay)) {
            if (isSlotAvailable(currentSlot, existingAppointments, schedule.getDuration())) {
                availableSlots.add(currentSlot);
            }
            currentSlot = currentSlot.plusMinutes(schedule.getDuration());
        }

        return availableSlots;
    }

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto requestDto) {

        //buscar el paciente por su id usando el repositorio
         User patient = this.userRepository.findById(requestDto.getPatientId())
                 .orElseThrow(() ->new UsernameNotFoundException("paciente no existe"));
          //buscar doctor por id
        User doctor = this.userRepository.findById(requestDto.getDoctorId())
                .orElseThrow(() -> new UsernameNotFoundException(" el doctor no existe"));

       if(!doctor.getRole().equals(Role.DOCTOR)){
           throw  new IllegalArgumentException("Tiene que se un doctor");
       }

        MedicalSchedule schedule = doctor.getMedicalSchedules();
        if (schedule == null) {
            throw new RuntimeException("El doctor no tiene un horario establecido");
        }

        if (requestDto.getDate().toLocalTime().isBefore(schedule.getStartTime()) ||
                requestDto.getDate().toLocalTime().isAfter(schedule.getEndTime())) {
            throw new RuntimeException("La cita no coindice con las horas de trabajo del doctor");
        }

        if (!isSlotAvailable(requestDto.getDate(),
                appointmentRepository.findByDoctorAndDateTimeBetween(
                        doctor,
                        requestDto.getDate(),
                        requestDto.getDate().plusMinutes(schedule.getDuration())
                ),
                schedule.getDuration())) {
            throw new RuntimeException("Este doctor no esta disponible");
        }

        //creacion de la cita
        MedicalAppointment appointment = new MedicalAppointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setReason(requestDto.getReason());
        appointment.setDateTime(requestDto.getDate());
        this.appointmentRepository.save(appointment);

        return AppointmentResponseDto.builder()
                .dateTime(appointment.getDateTime())
                .reason(appointment.getReason())
                .doctorId(requestDto.getDoctorId())
                .patientId(requestDto.getPatientId())
                .build();

    }

    private boolean isSlotAvailable(LocalDateTime slot, List<MedicalAppointment> existingAppointments, int duration) {
        LocalDateTime slotEnd = slot.plusMinutes(duration);
        return existingAppointments.stream()
                .noneMatch(appointment ->
                        (appointment.getDateTime().isEqual(slot) || appointment.getDateTime().isAfter(slot))
                                && appointment.getDateTime().isBefore(slotEnd));
    }
}
