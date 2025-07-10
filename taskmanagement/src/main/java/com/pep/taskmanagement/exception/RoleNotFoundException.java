package com.pep.taskmanagement.exception;


public class RoleNotFoundException extends RuntimeException{
    String message;

    public RoleNotFoundException(String message){
        super(message);
        this.message = message;
    }
}
