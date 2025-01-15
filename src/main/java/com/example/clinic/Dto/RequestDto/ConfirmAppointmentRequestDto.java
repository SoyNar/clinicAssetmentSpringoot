package com.example.clinic.Dto.RequestDto;

import com.example.clinic.Utils.AppointmentStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ConfirmAppointmentRequestDto {
    private Long appointmentId;
    private Long patientId;
    private String medications;
    private String diagnostics;
    private String notes;
    private AppointmentStatus statusAppointment;
}
