package com.be.javaassignment.repository;

import com.be.javaassignment.model.Metar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetarRepository extends JpaRepository<Metar, Long> {

    Optional<Metar> findTopBySubscriptionIdOrderByStoredAtDesc(Long subscriptionId);
}
