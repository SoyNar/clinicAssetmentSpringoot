package com.example.clinic.Dto.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@Builder
public class ScheduleResponseDto {
    private Long id;
    private String startTime;
    private String endTime;
    private int duration;
}
