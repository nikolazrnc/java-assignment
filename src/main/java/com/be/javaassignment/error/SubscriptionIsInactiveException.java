package com.be.javaassignment.error;

public class SubscriptionIsInactiveException extends RuntimeException{
    public SubscriptionIsInactiveException(String message){
        super(message);
    }
}
