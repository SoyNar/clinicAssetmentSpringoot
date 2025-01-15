package com.example.clinic.Service;


import com.example.clinic.Dto.RequestDto.AppointmentRequestDto;
import com.example.clinic.Dto.ResponseDto.AppointmentResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IAppointmentService {
    List<LocalDateTime> getAvailableSlot(Long doctorID, LocalDate date);
    AppointmentResponseDto createAppointment(AppointmentRequestDto requestDto);
    List<AppointmentResponseDto> getAppointmentByPatient(Long patientId);
    AppointmentResponseDto confirmAttention(AppointmentRequestDto requestDto);
}
