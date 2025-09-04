package com.be.javaassignment.dto;

import java.time.Instant;

public record MetarDto(Instant createdAt, String data, String icaoCode) {
}
