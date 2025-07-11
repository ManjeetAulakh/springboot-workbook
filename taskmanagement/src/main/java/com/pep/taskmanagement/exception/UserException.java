package com.pep.taskmanagement.exception;

public class UserException extends RuntimeException {
    String message;

    public UserException(String message) {
        super(message);
        this.message = message;
    }

}
