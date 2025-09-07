package com.be.javaassignment.dto.metar;

import java.time.Instant;

public record MetarRequestDto(Instant createdAt, String data) {
}
