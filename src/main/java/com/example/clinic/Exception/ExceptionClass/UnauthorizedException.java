package com.example.clinic.Exception.ExceptionClass;

public class UnauthorizedException extends RuntimeException{
    public UnauthorizedException(String message){
        super(message);
    }
}
