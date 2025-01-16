package com.example.clinic.Service;


import com.example.clinic.Dto.RequestDto.ScheduleRequestDto;
import com.example.clinic.Dto.ResponseDto.ScheduleResponseDto;


public interface IScheduleService {
    ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto);
}
