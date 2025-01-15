package com.example.clinic.Dto.RequestDto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AppointmentRequestDto {
//    private LocalTime startTime;
//    private LocalTime endTime;
    private Long doctorId;
    private Long patientId;
    private LocalDateTime date;
    private String reason;
}
