package com.be.javaassignment.service;

import com.be.javaassignment.dto.MetarDto;
import com.be.javaassignment.error.MetarDataNotFoundException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import com.be.javaassignment.mapper.MetarMapper;
import com.be.javaassignment.mapper.SubscriptionMapper;
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
    private final SubscriptionMapper subscriptionMapper;
    private final MetarMapper metarMapper;

    public MetarDto getMetarData(String icaoCode) {
        log.info("Fetching METAR data for airport: {}", icaoCode);
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> {
                log.error("No subscription found for airport: {}", icaoCode);
                return new SubscriptionNotFoundException("Subscription for code: " + icaoCode + " not found.");});

        Metar metarData = metarRepository.findLatestBySubscriptionId(subscription.getSubscriptionId()).orElseThrow(()-> {
            log.warn("No METAR data found for airport: {}", icaoCode);
            return new MetarDataNotFoundException("No METAR data found for airport: " + icaoCode);});
        log.info("Metar data fetched for airport: {}", icaoCode);
        return metarMapper.toDto(metarData);
    }

    public MetarDto addMetarData(String icaoCode, MetarDto metarDto) {
        log.info("Saving METAR data for airport: {}", icaoCode);
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> {
            log.error("No subscription found for airport: {}", icaoCode);
            return new SubscriptionNotFoundException("Subscription for code: " + icaoCode + " not found.");});
        Metar metar=metarMapper.toEntity(metarDto,subscription);
        metarRepository.save(metar);
        log.info("Saved METAR data for airport: {}", icaoCode);
        return metarMapper.toDto(metar);

    }
}
