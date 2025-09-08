package com.be.javaassignment.service.impl;

import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class MetarParserServiceImplTest {

    private MetarParserServiceImpl metarParserService;
    private Metar metar;

    @BeforeEach
    void setUp() {
        this.metarParserService=new MetarParserServiceImpl();
        Subscription subscription = new Subscription();
        subscription.setIcaoCode("LDZA");
        subscription.setActive(true);

        this.metar = new Metar();
        metar.setMetarId(1L);
        metar.setCreatedAt(Instant.parse("2025-10-06T12:00:00Z"));
        metar.setData("LDZA 070830Z VRB02KT CAVOK 20/13 Q1022 NOSIG");
        metar.setSubscription(subscription);
    }

    @Test
    @DisplayName("Should correctly parse METAR data")
    void shouldParseMetarCorrectly() {
        metarParserService.parseMetar(metar);

        assertEquals("Zagreb Airport", metar.getAirportName());
        assertEquals("October 6th, 2025 at 12:00 UTC", metar.getObservationTime());
        assertEquals("variable at 2 knots", metar.getWind());
        assertEquals("ceiling and visibility OK", metar.getVisibility());
        assertEquals("20°C", metar.getTemperature());
        assertEquals("13°C", metar.getDewPoint());
        assertEquals("1022 hPa", metar.getPressure());
        assertEquals("no significant changes expected", metar.getTrend());

        assertNull(metar.getCloudCoverage());
        assertNull(metar.getWeather());
        assertNull(metar.getRvr());
    }
}