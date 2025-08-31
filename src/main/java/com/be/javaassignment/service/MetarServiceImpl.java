package com.be.javaassignment.service;

import com.be.javaassignment.dto.MetarDto;
import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.MetarRepository;
import com.be.javaassignment.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class MetarServiceImpl implements MetarService{
    private final MetarRepository metarRepository;
    private final SubscriptionRepository subscriptionRepository;

    public MetarDto getMetarData(String icaoCode) {
        log.info("Fetching METAR data for airport: {}", icaoCode);
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> {
                log.error("No subscription found for airport: {}", icaoCode);
                return new IllegalArgumentException("Subscription for code: " + icaoCode + " not found.");});

        Metar metarData = metarRepository.findTopBySubscriptionIdOrderByStoredAtDesc(subscription.getSubscriptionId()).orElseThrow(()-> {
            log.warn("No METAR data found for airport: {}", icaoCode);
            return new IllegalArgumentException("No METAR data found for airport: " + icaoCode);});
        log.info("Metar data fetched for airport: {}", icaoCode);
        return new MetarDto(metarData.getStoredAt().toString(),metarData.getData());
    }

    public MetarDto addMetarData(String icaoCode, MetarDto metarDto) {
        log.info("Saving METAR data for airport: {}", icaoCode);
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> {
            log.error("No subscription found for airport: {}", icaoCode);
            return new IllegalArgumentException("Subscription for code: " + icaoCode + " not found.");});
        Metar metar = new Metar();
        metar.setSubscriptionId(subscription.getSubscriptionId());
        metar.setData(metarDto.data());
        metar.setStoredAt(Instant.parse(metarDto.createdAt()));
        metarRepository.save(metar);
        log.info("Saved METAR data for airport: {}", icaoCode);
        return new MetarDto(metar.getStoredAt().toString(),metar.getData());

    }
}
