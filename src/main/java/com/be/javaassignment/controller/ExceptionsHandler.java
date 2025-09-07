package com.be.javaassignment.controller;

import com.be.javaassignment.dto.ErrorDto;
import com.be.javaassignment.error.InvalidIcaoCodeFormatException;
import com.be.javaassignment.error.MetarDataNotFoundException;
import com.be.javaassignment.error.SubscriptionAlreadyExistsException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.Instant;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler(SubscriptionNotFoundException.class)
    public ResponseEntity<ErrorDto> handleSubscriptionNotFoundException(SubscriptionNotFoundException e){
        ErrorDto errorDto=new ErrorDto(Instant.now(), e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SubscriptionAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleSubscriptionAlreadyExistsException(SubscriptionAlreadyExistsException e){
        ErrorDto errorDto=new ErrorDto(Instant.now(), e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MetarDataNotFoundException.class)
    public ResponseEntity<ErrorDto> handleMetarDataNotFoundException(MetarDataNotFoundException e){
        ErrorDto errorDto=new ErrorDto(Instant.now(), e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception e){
        ErrorDto errorDto=new ErrorDto(Instant.now(), "Internal server error");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidIcaoCodeFormatException.class)
    public ResponseEntity<ErrorDto> handleInvalidIcaoCodeFormatException(InvalidIcaoCodeFormatException e) {
        ErrorDto errorDto = new ErrorDto(Instant.now(), e.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

}
