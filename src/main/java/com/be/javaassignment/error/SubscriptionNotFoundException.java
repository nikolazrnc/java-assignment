package com.be.javaassignment.error;

public class SubscriptionNotFoundException extends RuntimeException{
    public SubscriptionNotFoundException(String message){
        super(message);
    }
}
