package com.mchm.swp.exception;

public class ParentNotFoundException extends RuntimeException {
    public ParentNotFoundException(String message) {
        super("No Parent found with Auth Username: " + message);
    }
}
