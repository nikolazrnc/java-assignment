package com.be.javaassignment.error;

public class SubscriptionAlreadyExistsException extends RuntimeException{
    public SubscriptionAlreadyExistsException(String message){
        super(message);
    }
}
