package com.example.clinic.Dto.ResponseDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegisterUserResponseDto {

    private String name;
    private String lastname;
    private String email;
    private  Long id;
    private String role;

}
