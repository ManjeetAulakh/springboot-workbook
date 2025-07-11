package com.pep.taskmanagement.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(RoleException.class)
    public ResponseEntity<Map<String,String>> roleNotFoundHandler(RoleException e){

        Map<String,String> errors = new HashMap<>();
        errors.put("Error", "Role Exception");
        errors.put("message", e.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String,String>> userNotFoundHandler(UserException e){

        Map<String,String> errors = new HashMap<>();
        errors.put("Error", "User Exception");
        errors.put("message", e.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String,String>> illegalArgumentHandler(IllegalArgumentException e){

        Map<String,String> errors = new HashMap<>();
        errors.put("Error", "Illegal Argument Exception");
        errors.put("message", e.getMessage());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

}
