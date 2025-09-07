package com.be.javaassignment.service.impl;

import com.be.javaassignment.dto.metar.MetarRequestDto;
import com.be.javaassignment.dto.metar.MetarResponseDto;
import com.be.javaassignment.error.MetarDataNotFoundException;
import com.be.javaassignment.error.SubscriptionIsInactiveException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import com.be.javaassignment.mapper.MetarMapper;
import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.MetarRepository;
import com.be.javaassignment.repository.SubscriptionRepository;
import com.be.javaassignment.service.MetarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MetarServiceImpl implements MetarService {
    private final MetarRepository metarRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final MetarMapper metarMapper;

    public MetarResponseDto getMetarData(String icaoCode) {
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> {
                log.error("No subscription found for airport with ICAO code {}", icaoCode);
                return new SubscriptionNotFoundException("Subscription for airport with ICAO code " + icaoCode + " not found.");});

        if(!subscription.isActive()){
            log.warn("Subscription for airport with ICAO code {} is currently inactive.", icaoCode);
            throw new SubscriptionIsInactiveException("Subscription for airport with ICAO "+ icaoCode + " is currently inactive");
        }
        Metar metarData = metarRepository.findTopBySubscription_SubscriptionIdOrderByCreatedAtDesc(subscription.getSubscriptionId()).orElseThrow(()-> {
            log.warn("No METAR data found for airport with ICAO code {}", icaoCode);
            return new MetarDataNotFoundException("No METAR data found for airport with ICAO code " + icaoCode);});
        log.info("METAR data fetched for airport with ICAO code {}", icaoCode);
        return metarMapper.toResponseDto(metarData);
    }

    public MetarResponseDto addMetarData(String icaoCode, MetarRequestDto metarRequestDto) {
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> {
            log.error("No subscription found for airport with ICAO code {}", icaoCode);
            return new SubscriptionNotFoundException("Subscription for airport with ICAO code " + icaoCode + " not found.");});
        Metar metar=metarMapper.toEntity(metarRequestDto,subscription);
        metarRepository.save(metar);
        log.info("Saved METAR data for airport with ICAO code {}", icaoCode);
        return metarMapper.toResponseDto(metar);

    }
}
