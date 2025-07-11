package com.pep.taskmanagement.exception;

public class PageException extends RuntimeException {
    String message;
    public PageException(String message){
        super(message);
        this.message = message;
    }
}
