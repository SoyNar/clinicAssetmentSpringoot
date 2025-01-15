package com.example.clinic.Exception.ExceptionClass;


public class UserNotFoundException  extends RuntimeException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
