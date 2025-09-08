package com.be.javaassignment.dto.metar;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;


@JsonInclude(JsonInclude.Include.NON_NULL)
public record MetarResponseDto(Long metarId, Instant createdAt, Long subscriptionId, String data,String airportName,
                                      String observationTime, String wind, String windVariation,
                                      String visibility, String cloudCoverage,
                                      String weather, String temperature,
                                      String dewPoint, String pressure, String rvr, String trend){
}