package com.example.clinic.Service.Impl;

import com.example.clinic.Domain.User;
import com.example.clinic.Dto.RequestDto.RegisterUserRequestDto;
import com.example.clinic.Dto.ResponseDto.RegisterUserResponseDto;
import com.example.clinic.Exception.ExceptionClass.EmailAlreadyExistException;
import com.example.clinic.Repository.IUserRepository;
import com.example.clinic.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserSiviceImpl implements IUserService {

    //inyectar dependencia del repositorio a través del constructor

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserSiviceImpl(IUserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder =passwordEncoder;
    }

    @Transactional
    @Override
    public RegisterUserResponseDto registerUser(RegisterUserRequestDto requestDto) {
        //validar si el email del usuario ya esta registrado en la base de datos

      if(this.userRepository.findByEmail(requestDto.getEmail()).isPresent()){
         throw  new EmailAlreadyExistException("Usuario ya existe");
      }


        //crear al usuario usando el password encoder para encriptar la contraseña

        User user = User.builder()
                .fullname(requestDto.getName())
                .lastname(requestDto.getLastname())
                .email(requestDto.getEmail())
                .role(requestDto.getRole())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        this.userRepository.save(user);

        return RegisterUserResponseDto.builder()
                .name(user.getFullname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(String.valueOf(user.getRole()))
                .id(user.getId())
                .build();
    }

    @Override
    public List<RegisterUserResponseDto> getALlUsers() {
        List<User> users = this.userRepository.findAll();

        return users.stream()
                .map( usersModel -> RegisterUserResponseDto.builder()
                        .name(usersModel.getFullname())
                        .lastname(usersModel.getLastname())
                        .email(usersModel.getEmail())
                        .role(usersModel.getRole().name())
                        .id(usersModel.getId())
                        .build())
                .collect(Collectors.toList());
    }

}
