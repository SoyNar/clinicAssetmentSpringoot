package com.example.clinic.Dto.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class AppointmentResponseDto {
    private LocalDateTime dateTime;
    private String reason;
    private Long doctorId;
    private Long patientId;
}
