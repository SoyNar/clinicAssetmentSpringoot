package com.example.clinic.Controller;

import com.example.clinic.Domain.MedicalSchedule;
import com.example.clinic.Dto.RequestDto.AppointmentRequestDto;
import com.example.clinic.Dto.RequestDto.ConfirmAppointmentRequestDto;
import com.example.clinic.Dto.ResponseDto.AppointmentResponseDto;
import com.example.clinic.Dto.ResponseDto.ConfirmAppointmentResponseDto;
import com.example.clinic.Exception.ExceptionClass.BaRequestException;
import com.example.clinic.Service.IAppointmentService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final IAppointmentService appointmentService;

    @Autowired
    public UserController(IAppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @GetMapping("/available-slots")
    public ResponseEntity<List<LocalDateTime>> getAvailableSlots(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(appointmentService.getAvailableSlot(doctorId, date));
    }

    @PostMapping("/appointment")
    public ResponseEntity<AppointmentResponseDto> createAppointment(
           @RequestBody AppointmentRequestDto requestDto) {
        return ResponseEntity.ok(appointmentService.createAppointment(requestDto));
    }

    @PutMapping("/confirm/{appointmentId}/appointments")
    public ResponseEntity<ConfirmAppointmentResponseDto> confirmAttention(
            @PathVariable Long appointmentId,
            @RequestBody ConfirmAppointmentRequestDto requestDto) {
        if (!appointmentId.equals(requestDto.getAppointmentId())) {
            throw new BaRequestException("url incorrecta");
        }
        ConfirmAppointmentResponseDto response = appointmentService.confirmAttention(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
