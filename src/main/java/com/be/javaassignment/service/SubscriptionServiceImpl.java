package com.be.javaassignment.service;

import com.be.javaassignment.dto.SubscriptionDto;
import com.be.javaassignment.error.SubscriptionAlreadyExistsException;
import com.be.javaassignment.error.SubscriptionNotFoundException;
import com.be.javaassignment.mapper.SubscriptionMapper;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    public SubscriptionDto addSubscription(SubscriptionDto subscriptionDto) {
        String icaoCode = subscriptionDto.icaoCode().toUpperCase();
        log.info("Adding subscription for airport: {}", subscriptionDto.icaoCode());
        subscriptionRepository.findByIcaoCode(icaoCode).ifPresent(subscription -> {
            log.warn("Subscription already exists for airport: {}", icaoCode);
            throw new SubscriptionAlreadyExistsException("Subscription for airport "+ icaoCode + " already exists.");});

        Subscription subscription = new Subscription();
        subscription.setIcaoCode(subscriptionDto.icaoCode());
        subscriptionRepository.save(subscription);
        log.info("Subscription saved for airport: {}", icaoCode);
        return subscriptionMapper.toDto(subscription);

    }

    public SubscriptionDto deleteSubscription(String icaoCode) {
        log.info("Deleting subscription for airport: {}", icaoCode);
        Subscription deleteSubscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(() -> {
            log.error("Subscription not found for airport {}", icaoCode);
            return new SubscriptionNotFoundException("Subscription not found for: " + icaoCode);
        });
        subscriptionRepository.delete(deleteSubscription);
        log.info("Deleted subscription for airport: {}", icaoCode);
        return subscriptionMapper.toDto(deleteSubscription);
    }

    public List<SubscriptionDto> getSubscriptions() {
        log.info("Fetching subscriptions from data base");
        return subscriptionRepository.findAll().stream().map(subscriptionMapper::toDto).collect(Collectors.toList());
    }
}
