package com.example.assignmentpwc.exception.handler;

import com.example.assignmentpwc.exception.NoPossibleRouteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

// Handle all the logical unchecked run-time exceptions
@ControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(NoPossibleRouteException.class)
    public ResponseEntity<Map> handleNotfoundException(NoPossibleRouteException exception) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map> handleException(Exception exception) {
        Map<String, String> response = new HashMap<>();
        response.put("message", exception.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

}
