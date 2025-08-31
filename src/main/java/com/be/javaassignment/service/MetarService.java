package com.be.javaassignment.service;

import com.be.javaassignment.dto.MetarDto;
import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.MetarRepository;
import com.be.javaassignment.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@AllArgsConstructor
public class MetarService {
    private final MetarRepository metarRepository;
    private final SubscriptionRepository subscriptionRepository;

    public MetarDto getMetarData(String icaoCode) {
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> new IllegalArgumentException("Subscription for code: " + icaoCode + "not found."));
        Metar metarData = metarRepository.findTopBySubscriptionIdOrderByStoredAtDesc(subscription.getSubscriptionId()).orElseThrow(()-> new IllegalArgumentException("No METAR data found for airport: " + icaoCode));
        return new MetarDto(metarData.getData());
    }

    public MetarDto addMetarData(String icaoCode, MetarDto metarDto) {
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> new IllegalArgumentException("Subscription for code: " + icaoCode + "not found."));
        Metar metar = new Metar();
        metar.setSubscriptionId(subscription.getSubscriptionId());
        metar.setData(metarDto.data());
        metar.setStoredAt(Instant.now());
        metarRepository.save(metar);
        return new MetarDto(metar.getData());

    }
}
