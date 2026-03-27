package com.mchm.swp.exception;

public class FacultyNotFoundException extends RuntimeException {
    public FacultyNotFoundException(String message) {
        super("No Faculty found with Auth Username: " + message);
    }
}
