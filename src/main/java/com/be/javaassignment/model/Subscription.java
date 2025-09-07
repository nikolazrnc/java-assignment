package com.be.javaassignment.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity(name = "subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subscriptionId;
    @Column(name="icao_code",nullable = false,unique = true, length = 4)
    @Pattern(regexp="^[A-Za-z]{4}$")
    private String icaoCode;
}
