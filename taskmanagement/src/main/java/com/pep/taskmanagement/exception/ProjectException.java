package com.pep.taskmanagement.exception;

public class ProjectException extends RuntimeException {
    String message;
    public ProjectException(String message){
        super(message);
        this.message = message;
    }
}
