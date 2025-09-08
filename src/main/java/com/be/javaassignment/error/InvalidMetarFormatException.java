package com.be.javaassignment.error;

public class InvalidMetarFormatException extends RuntimeException {
    public InvalidMetarFormatException(String message) {
        super(message);
    }
}
