package com.example.clinic.Service;

import com.example.clinic.Dto.RequestDto.RegisterUserRequestDto;
import com.example.clinic.Dto.ResponseDto.RegisterUserResponseDto;

import java.util.List;

public interface IUserService {
    RegisterUserResponseDto registerUser(RegisterUserRequestDto requestDto);
    List<RegisterUserResponseDto> getALlUsers();

}
