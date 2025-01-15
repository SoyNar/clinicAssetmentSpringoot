package com.example.clinic.Service;


import com.example.clinic.Dto.RequestDto.ScheduleRequestDto;
import com.example.clinic.Dto.ResponseDto.RegisterUserResponseDto;
import com.example.clinic.Dto.ResponseDto.ScheduleResponseDto;

import java.util.List;

public interface IScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto);
}
