package com.be.javaassignment.dto.metar;

public record MetarRequestFilterDto(Boolean metarId, Boolean createdAt, Boolean subscriptionId, Boolean data, Boolean airportName,
                                    Boolean observationTime, Boolean wind, Boolean windVariation,
                                    Boolean visibility, Boolean cloudCoverage,
                                    Boolean weather, Boolean temperature,
                                    Boolean dewPoint, Boolean pressure, Boolean rvr, Boolean trend){
}
