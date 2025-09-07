package com.be.javaassignment.error;

public class InvalidIcaoCodeFormatException extends RuntimeException{
    public InvalidIcaoCodeFormatException(String message){
        super(message);
    }
}
