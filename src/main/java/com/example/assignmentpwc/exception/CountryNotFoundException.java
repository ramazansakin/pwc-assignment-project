package com.example.assignmentpwc.exception;

public class CountryNotFoundException extends RuntimeException {
    public CountryNotFoundException(String country) {
        super("Country not found [ " + country + " ]");
    }
}
