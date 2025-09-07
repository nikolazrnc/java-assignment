package com.be.javaassignment.service.impl;

import com.be.javaassignment.dto.subscription.SubscriptionFilterDto;
import com.be.javaassignment.dto.subscription.SubscriptionRequestDto;
import com.be.javaassignment.dto.subscription.SubscriptionResponseDto;
import com.be.javaassignment.dto.subscription.SubscriptionStatusDto;
import com.be.javaassignment.error.InvalidIcaoCodeFormatException;
import com.be.javaassignment.error.SubscriptionAlreadyExistsException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import com.be.javaassignment.mapper.SubscriptionMapper;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.SubscriptionRepository;
import com.be.javaassignment.service.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    public SubscriptionResponseDto addSubscription(SubscriptionRequestDto subscriptionRequestDto) {
        String icaoCode = subscriptionRequestDto.icaoCode().toUpperCase();
        if(!icaoCode.matches("^[A-Za-z]{4}$")){
            throw new InvalidIcaoCodeFormatException(
                    "Invalid ICAO code format. ICAO code must contain exactly 4 letters: "+ icaoCode
            );
        }
        Optional<Subscription> oldSubscription=subscriptionRepository.findByIcaoCode(icaoCode);
        if(oldSubscription.isPresent()){
            Subscription sub= oldSubscription.get();
            if(sub.isActive()){
                log.warn("Subscription already exists and is active for airport with ICAO code {}", icaoCode);
                throw new SubscriptionAlreadyExistsException("Subscription for airport with ICAO code " + icaoCode + " already exists.");
            } else {
                log.info("Reactivating existing inactive subscription for airport with ICAO code {}", icaoCode);
                sub.setActive(true);
                subscriptionRepository.save(sub);
                return subscriptionMapper.toDto(sub);
            }
        }

        Subscription subscription = new Subscription();
        subscription.setIcaoCode(subscriptionRequestDto.icaoCode());
        subscription.setActive(true);
        subscriptionRepository.save(subscription);
        log.info("Subscription saved for airport with ICAO code {}", icaoCode);
        return subscriptionMapper.toDto(subscription);

    }

    @Override
    public SubscriptionResponseDto deleteSubscription(String icaoCode) {
        Subscription deleteSubscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(() -> {
            log.error("Subscription not found for airport with ICAO code {}", icaoCode);
            return new SubscriptionNotFoundException("Subscription not found for airport with ICAO code " + icaoCode);
        });
        subscriptionRepository.delete(deleteSubscription);
        log.info("Deleted subscription for airport with ICAO code {}", icaoCode);
        return subscriptionMapper.toDto(deleteSubscription);
    }

    @Override
    public List<SubscriptionResponseDto> getSubscriptions(SubscriptionFilterDto filter) {
        return subscriptionRepository.findAll().stream()
                .filter(sub -> filter.active()==null || sub.isActive() == filter.active())
                .filter(sub -> filter.name()==null || sub.getIcaoCode().contains(filter.name().toUpperCase()))
                .map(subscriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionResponseDto updateSubscriptionStatus(String icaoCode, SubscriptionStatusDto statusDto) {
        Subscription toUpdate = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(() -> {
            log.error("Subscription not found for airport with ICAO code {}", icaoCode);
            return new SubscriptionNotFoundException("Subscription not found for airport with ICAO code " + icaoCode);
        });
        toUpdate.setActive(statusDto.active());
        subscriptionRepository.save(toUpdate);

        return subscriptionMapper.toDto(toUpdate);
    }
}
