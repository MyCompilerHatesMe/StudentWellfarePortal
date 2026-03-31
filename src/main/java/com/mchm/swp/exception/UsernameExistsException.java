package com.mchm.swp.exception;

public class UsernameExistsException extends RuntimeException {
    public UsernameExistsException(String message) {
        super("Username " + message + " already exists");
    }
}
