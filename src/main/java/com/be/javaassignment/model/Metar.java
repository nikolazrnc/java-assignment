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
    private Instant storedAt;
    private Long subscriptionId;
    private String data;
}
