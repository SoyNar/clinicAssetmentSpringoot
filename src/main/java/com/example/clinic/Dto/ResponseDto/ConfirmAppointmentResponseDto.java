package com.example.clinic.Dto.ResponseDto;

import com.example.clinic.Utils.AppointmentStatus;
import lombok.*;


@Getter
@Setter
@Builder

public class ConfirmAppointmentResponseDto {

    private String notes;
    private String diagnostics;
    private String medications;
    private String reason;
    private Long id;
    private Long patientId;
    private Long doctorId;
    private AppointmentStatus status;
}
