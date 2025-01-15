package com.example.clinic.Dto.RequestDto;

import com.example.clinic.Domain.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter
@Setter
@Builder
public class RegisterUserRequestDto {

    @NotBlank(message = "nombre requerido")
    private String name;

    private String lastname;
    @NotBlank(message = "email requerido")

    private String email;
    @NotBlank(message = "contraseña requerida")

    private String password;

    private Role role;

}
