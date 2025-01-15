package com.example.clinic.Service.Impl;

import com.example.clinic.Domain.MedicalSchedule;
import com.example.clinic.Domain.User;
import com.example.clinic.Dto.RequestDto.ScheduleRequestDto;
import com.example.clinic.Dto.ResponseDto.RegisterUserResponseDto;
import com.example.clinic.Dto.ResponseDto.ScheduleResponseDto;
import com.example.clinic.Exception.ExceptionClass.InvalidScheduleTimeException;
import com.example.clinic.Exception.ExceptionClass.UserNotFoundException;
import com.example.clinic.Repository.IScheduleRepository;
import com.example.clinic.Repository.IUserRepository;
import com.example.clinic.Service.IScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements IScheduleService {

    private final IScheduleRepository scheduleRepository;
    private final IUserRepository userRepository;

    @Autowired
    public ScheduleServiceImpl(IScheduleRepository scheduleRepository, IUserRepository userRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ScheduleResponseDto createSchedule(ScheduleRequestDto requestDto) {
        // se debe proporcionar el id del medico al que se le creara el horario
        // se debe proporcionar la hora de inicio de trabajo y la hora de fin, ademas cuanto dura la cita medica

        //verificar si el medico ya tiene un horario asignado
        // validar si la hora de entrada es mayor que la hora de salida

        //verificar si el medico existe

       User doctor = this.userRepository.findById(requestDto.getDoctorId())
               .orElseThrow(() -> new UserNotFoundException(" doctor no encontrdado"));


       //verificar si la hora de inicio es mayor que la hora de salis usando el metodo isAfter
if(requestDto.getStartTime().isAfter(requestDto.getEndTime())){
    throw new InvalidScheduleTimeException(" la hora de inicio no puder ser mayo a la hora de fin");
}

   //validar el numero puesto en la duracion de la cita medica que no puede ser menor  a cero
        if(requestDto.getDuration() <= 0){
          throw   new InvalidScheduleTimeException("la duracion no puede ser cero o menor a cero ");
        }



        MedicalSchedule medicalSchedule = MedicalSchedule.builder()
                .doctor(doctor)
                .startTime(requestDto.getStartTime())
                .endTime(requestDto.getEndTime())
                .duration(requestDto.getDuration())
                .build();
         MedicalSchedule save = this.scheduleRepository.save(medicalSchedule);
        return ScheduleResponseDto.builder()
                .id(save.getId())
                .startTime(String.valueOf(save.getStartTime()))
                .endTime(String.valueOf(save.getEndTime()))
                .duration(save.getDuration())
                .build();
    }


    //pendiente validar que los campos del request no esten vacios
    //pendiente validar que el usuario sea un medico osea tenga rol doctor

}
