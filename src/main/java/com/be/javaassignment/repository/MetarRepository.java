package com.be.javaassignment.repository;

import com.be.javaassignment.model.Metar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetarRepository extends JpaRepository<Metar, Long> {

    @Query("SELECT m FROM metar m WHERE m.subscription.subscriptionId=:subscriptionId ORDER BY m.createdAt DESC LIMIT 1")
    Optional<Metar> findLatestBySubscriptionId(Long subscriptionId);

}
