package com.be.javaassignment.service.impl;

import com.be.javaassignment.dto.metar.MetarRequestDto;
import com.be.javaassignment.dto.metar.MetarRequestFilterDto;
import com.be.javaassignment.dto.metar.MetarResponseDto;
import com.be.javaassignment.error.InvalidMetarFormatException;
import com.be.javaassignment.error.MetarDataNotFoundException;
import com.be.javaassignment.error.SubscriptionIsInactiveException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import com.be.javaassignment.mapper.MetarMapper;
import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.MetarRepository;
import com.be.javaassignment.repository.SubscriptionRepository;
import com.be.javaassignment.service.MetarParserService;
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
    private final MetarParserService metarParserService;

    @Override
    public MetarResponseDto getMetarData(String icaoCode) {
        Subscription subscription = getActiveSubscription(icaoCode);
        Metar metarData = getLatestMetar(subscription.getSubscriptionId(), icaoCode);

        log.info("METAR data fetched for airport with ICAO code {}", icaoCode);
        return metarMapper.toResponseDto(metarData);
    }

    @Override
    public MetarResponseDto addMetarData(String icaoCode, MetarRequestDto metarRequestDto) {
        String[] parts = metarRequestDto.data().trim().split("\\s+");
        if (parts.length == 0 || !parts[0].equalsIgnoreCase(icaoCode)) {
            log.error("METAR data is empty or ICAO code {} does not match METAR data code {}",
                    icaoCode, parts.length > 0 ? parts[0] : "N/A");
            throw new InvalidMetarFormatException("METAR data is empty or ICAO codes don't match");
        }

        Subscription subscription = getActiveSubscription(icaoCode);
        Metar metar = metarMapper.toEntity(metarRequestDto, subscription);
        metarParserService.parseMetar(metar);
        metarRepository.save(metar);

        log.info("Saved METAR data for airport with ICAO code {}", icaoCode);
        return metarMapper.toResponseDto(metar);
    }

    @Override
    public MetarResponseDto getFilteredMetarData(String icaoCode, MetarRequestFilterDto filter) {
        Subscription subscription = getActiveSubscription(icaoCode);
        Metar metarData = getLatestMetar(subscription.getSubscriptionId(), icaoCode);
        log.info("Filtered METAR data fetched for airport: {}", icaoCode);
        Metar filteredData = new Metar();
        try {
            for(var field : MetarRequestFilterDto.class.getDeclaredFields()) {
                field.setAccessible(true);
                Object filterValue=field.get(filter);
                if(filterValue != null) {
                    var entityField = Metar.class.getDeclaredField(field.getName());
                    entityField.setAccessible(true);
                    if("subscriptionId".equals(field.getName())){
                        entityField.set(filteredData, metarData.getSubscription());
                    }else{
                        if(entityField.get(metarData)!=null){
                            entityField.set(filteredData, entityField.get(metarData));
                        } else{
                            entityField.set(filteredData, "No data found");
                        }
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("Failed to filter METAR data", e);
        }
        return metarMapper.toResponseDto(filteredData);

    }

    private Subscription getActiveSubscription(String icaoCode) {
        Subscription subscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase())
                .orElseThrow(() -> {
                    log.error("No subscription found for airport with ICAO code {}", icaoCode);
                    return new SubscriptionNotFoundException("Subscription for airport with ICAO code " + icaoCode + " not found.");
                });

        if (!subscription.isActive()) {
            log.warn("Subscription for airport with ICAO code {} is currently inactive.", icaoCode);
            throw new SubscriptionIsInactiveException("Subscription for airport with ICAO " + icaoCode + " is currently inactive");
        }
        return subscription;
    }

    private Metar getLatestMetar(Long subscriptionId, String icaoCode) {
        return metarRepository.findTopBySubscription_SubscriptionIdOrderByCreatedAtDesc(subscriptionId)
                .orElseThrow(() -> {
                    log.warn("No METAR data found for airport with ICAO code {}", icaoCode);
                    return new MetarDataNotFoundException("No METAR data found for airport with ICAO code " + icaoCode);
                });
    }


}


