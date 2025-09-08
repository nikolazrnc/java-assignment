package com.be.javaassignment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@Data
@Entity(name = "metar")
public class Metar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metarId;
    @Column(nullable = false)
    private Instant createdAt;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="subscription_id", nullable = false)
    private Subscription subscription;
    @Column(nullable = false)
    private String data;

    private String airportName;
    private String observationTime;
    private String wind;
    private String windVariation;
    private String visibility;
    private String cloudCoverage;
    private String weather;
    private String temperature;
    private String dewPoint;
    private String pressure;
    private String rvr;
    private String trend;
}
