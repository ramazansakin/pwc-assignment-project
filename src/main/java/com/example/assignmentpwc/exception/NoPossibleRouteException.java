package com.example.assignmentpwc.exception;

public class NoPossibleRouteException extends RuntimeException {
    public NoPossibleRouteException(String source, String destination) {
        super("There is no possible route from " + source + " to " + destination);
    }
}
