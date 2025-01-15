package com.example.clinic.Controller;

import com.example.clinic.Dto.RequestDto.RegisterUserRequestDto;
import com.example.clinic.Dto.RequestDto.ScheduleRequestDto;
import com.example.clinic.Dto.ResponseDto.AppointmentResponseDto;
import com.example.clinic.Dto.ResponseDto.RegisterUserResponseDto;
import com.example.clinic.Dto.ResponseDto.ScheduleResponseDto;
import com.example.clinic.Service.IAppointmentService;
import com.example.clinic.Service.IScheduleService;
import com.example.clinic.Service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final IScheduleService scheduleService;
    private final IUserService userService;
    private final IAppointmentService appointmentService;

    @Autowired
    public AdminController(IScheduleService scheduleService, IUserService userService, IAppointmentService appointmentService) {
        this.scheduleService = scheduleService;
        this.userService = userService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/schedule")
    public ResponseEntity<ScheduleResponseDto> createSchedule( @RequestBody  ScheduleRequestDto requestDto){
        ScheduleResponseDto responseDto = this.scheduleService.createSchedule(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    //registrar un usuario cualquier rol

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> registerUser ( @RequestBody RegisterUserRequestDto requestDto){
        RegisterUserResponseDto registerUser = this.userService.registerUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
    }

    @GetMapping("/allusers")
    public ResponseEntity<List<RegisterUserResponseDto>> getAllUsers(){
        List<RegisterUserResponseDto> getUsers = this.userService.getALlUsers();
        return ResponseEntity.status(HttpStatus.OK).body(getUsers);
    }

    @GetMapping("/appointment/{patientId}")
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointmentPatient(
            @PathVariable Long patientId){
        List<AppointmentResponseDto> appointments = this.appointmentService.getAppointmentByPatient(patientId);
        return ResponseEntity.status(HttpStatus.OK).body(appointments);
    }
}
