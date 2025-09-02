package com.be.javaassignment.error;

public class MetarDataNotFoundException extends RuntimeException{
    public MetarDataNotFoundException(String message){
        super(message);
    }
}
