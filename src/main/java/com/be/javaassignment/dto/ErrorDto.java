package com.be.javaassignment.dto;

import java.time.Instant;

public record ErrorDto (Instant timestamp, String message){
}
