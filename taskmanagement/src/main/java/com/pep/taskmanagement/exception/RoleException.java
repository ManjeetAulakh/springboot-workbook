package com.pep.taskmanagement.exception;


public class RoleException extends RuntimeException{
    String message;

    public RoleException(String message){
        super(message);
        this.message = message;
    }
}
