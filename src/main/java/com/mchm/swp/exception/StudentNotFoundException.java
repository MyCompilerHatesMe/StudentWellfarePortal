package com.mchm.swp.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String message) {
        super("No student found with Auth Username: " + message);
    }
}
