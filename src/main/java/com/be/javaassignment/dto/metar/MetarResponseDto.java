package com.be.javaassignment.dto.metar;

import java.time.Instant;

public record MetarResponseDto (Long metarId, Instant createdAt, String data, Long subscriptionId){
}
