package com.example.clinic.Exception;

import com.example.clinic.Exception.ExceptionClass.EmailAlreadyExistException;
import com.example.clinic.Exception.ExceptionClass.InvalidScheduleTimeException;
import com.example.clinic.Exception.ExceptionClass.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerExceptions {

    //lanzadores

    //lanzador para cuando no se encuentra un usuario
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlerNotFound(UserNotFoundException exception){
        ErrorResponse error = ErrorResponse.builder()
                .message(exception.getMessage())
                .code(HttpStatus.NOT_FOUND.value())
                .error("USUARIO NO ENCONTRADO")
                .build();
        return  new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //lanzador para manejar cuando el inicio es mayor que el final

    @ExceptionHandler(InvalidScheduleTimeException.class)
    public ResponseEntity<ErrorResponse> handleIlvalidScheduleTime(InvalidMediaTypeException exception){
        ErrorResponse error = ErrorResponse.builder()
                .error(exception.getMessage())
                .code(HttpStatus.BAD_REQUEST.value())
                .message(exception.getMessage())
                .build();
        return  new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //lanzador para excepciones genreales
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception exception){
        ErrorResponse error = ErrorResponse.builder()
                .message(exception.getMessage())
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Error inesperado")
                .build();
              return  new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handlerEmailAlreadyExistException(EmailAlreadyExistException exception){
        ErrorResponse error = ErrorResponse.builder()
                .message(exception.getMessage())
                .code(HttpStatus.CONFLICT.value())
                .error("USUARIO YA EXISTE")
                .build();
              return new ResponseEntity<>(error, HttpStatus.CONFLICT);

    }
}
