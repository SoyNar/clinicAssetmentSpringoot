package com.example.clinic.Dto.RequestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class ScheduleRequestDto {

    @NotNull(message = "tiempo oblogatorio")
    private LocalTime startTime;
    @NotNull(message = "tiempo oblogatorio")
//formato HH:mm
    private LocalTime endTime;
    @NotNull(message = "duracion oblogatoria")

    private Integer duration;
    @NotNull(message = "id obligatorio")
    private Long doctorId;
}
