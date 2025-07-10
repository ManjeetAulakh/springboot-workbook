package com.pep.taskmanagement.exception;

public class UserAlreadyExistsException extends RuntimeException {
    String message;

    public UserAlreadyExistsException(String message){
        super(message);
        this.message = message;
    }
}
