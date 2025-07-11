package com.pep.taskmanagement.exception;

public class TaskException extends RuntimeException {
    String message;
    public TaskException(String message){
        super(message);
        this.message = message;
    }
}
