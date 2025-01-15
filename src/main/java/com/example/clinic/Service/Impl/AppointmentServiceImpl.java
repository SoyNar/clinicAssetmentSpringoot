package com.example.clinic.Service.Impl;

import com.example.clinic.Domain.MedicalAppointment;
import com.example.clinic.Domain.MedicalSchedule;
import com.example.clinic.Domain.Role;
import com.example.clinic.Domain.User;
import com.example.clinic.Dto.RequestDto.AppointmentRequestDto;
import com.example.clinic.Dto.RequestDto.ConfirmAppointmentRequestDto;
import com.example.clinic.Dto.ResponseDto.AppointmentResponseDto;
import com.example.clinic.Dto.ResponseDto.ConfirmAppointmentResponseDto;
import com.example.clinic.Exception.ExceptionClass.BaRequestException;
import com.example.clinic.Exception.ExceptionClass.UnauthorizedException;
import com.example.clinic.Exception.ExceptionClass.UserNotFoundException;
import com.example.clinic.Repository.IAppointmentRepository;
import com.example.clinic.Repository.IUserRepository;
import com.example.clinic.Service.IAppointmentService;
import com.example.clinic.Utils.AppointmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Slf4j
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
        log.info("Buscando doctor");
        User doctor = userRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
       log.info("doctor con id:{} no encontrado", doctor);
        if (!doctor.getRole().equals(Role.DOCTOR)) {
            throw new IllegalArgumentException("Tiene que se un doctor");
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
                .orElseThrow(() -> new UsernameNotFoundException("paciente no existe"));
        //buscar doctor por id
        User doctor = this.userRepository.findById(requestDto.getDoctorId())
                .orElseThrow(() -> new UsernameNotFoundException(" el doctor no existe"));

        if (!doctor.getRole().equals(Role.DOCTOR)) {
            throw new IllegalArgumentException("Tiene que se un doctor");
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
        appointment.setStatusAppointment(AppointmentStatus.PENDING);
        this.appointmentRepository.save(appointment);

        return AppointmentResponseDto.builder()
                .dateTime(appointment.getDateTime())
                .reason(appointment.getReason())
                .doctorId(requestDto.getDoctorId())
                .patientId(requestDto.getPatientId())
                .status(String.valueOf(appointment.getStatusAppointment()))
                .build();

    }

    @Override
    public List<AppointmentResponseDto> getAppointmentByPatient(Long patientId) {

    log.info("buscando paciente con id : {}",patientId);

     User user =  this.userRepository.findById(patientId)
                .orElseThrow(() -> new UserNotFoundException("paciente no encontrado"));

        // Validación del rol
        if (!user.getRole().equals(Role.PATIENT)) {
            throw new IllegalArgumentException("El usuario no tiene rol de paciente");
        }

        List<MedicalAppointment> appointment = this.appointmentRepository.findByPatientId(patientId);
        if (appointment.isEmpty()) {
            throw new IllegalArgumentException("Paciente no tiene citas asignadas");
        }


        return appointment.stream()
                .map(appDto ->
                        AppointmentResponseDto.builder()
                                .patientId(appDto.getPatient().getId())
                                .status(String.valueOf(appDto.getStatusAppointment()))
                                .dateTime(appDto.getDateTime())
                                .doctorId(appDto.getDoctor().getId())
                                .reason(appDto.getReason())
                                .build())
                .collect(Collectors.toList());
}
//metodo para confirmar la atencion de una cita
    @Override
    public ConfirmAppointmentResponseDto confirmAttention(ConfirmAppointmentRequestDto requestDto) {

//        // Obtener el doctor autenticado
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String currentUserEmail = authentication.getName(); /

        MedicalAppointment appointemenFind = this.appointmentRepository.findById(requestDto.getAppointmentId()).orElseThrow(()
                -> new RuntimeException("Cita no encontrada"));

        //validar que la cita pertenece al medico
        if (!appointemenFind.getDoctor().getId().equals(requestDto.getDoctorId())) {
            throw new UnauthorizedException("No esta autorizado");
        }

        //validar qeu la cita no ha sido atendida anteiromente
        if (appointemenFind.getStatusAppointment() == AppointmentStatus.ATTENDED) {
            throw new BaRequestException("Esta cita ya ha sido atendida");
        }

        //crear entrada en la base de datos
        //seria una actualziacion
        appointemenFind.setDiagnostics(requestDto.getDiagnostics());
        appointemenFind.setMedications(requestDto.getMedications());
        appointemenFind.setNotes(requestDto.getNotes());
        appointemenFind.setStatusAppointment(AppointmentStatus.ATTENDED);

        MedicalAppointment updatedAppointment =  this.appointmentRepository.save(appointemenFind);

        return ConfirmAppointmentResponseDto
                .builder()
                .patientId(updatedAppointment.getPatient().getId())
                .notes(updatedAppointment.getNotes())
                .medications(updatedAppointment.getMedications())
                .doctorId(updatedAppointment.getDoctor().getId())
                .reason(updatedAppointment.getReason())
                .status(updatedAppointment.getStatusAppointment())
                .diagnostics(updatedAppointment.getDiagnostics())
                .id(updatedAppointment.getId())
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
