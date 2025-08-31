package com.be.javaassignment.service;

import com.be.javaassignment.dto.SubscriptionDto;
import com.be.javaassignment.model.Subscription;
import com.be.javaassignment.repository.SubscriptionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionDto addSubscription(SubscriptionDto subscriptionDto) {
        String icaoCode = subscriptionDto.icaoCode().toUpperCase();
        subscriptionRepository.findByIcaoCode(icaoCode).ifPresent(subscription -> {throw new IllegalArgumentException("Subscription for airport "+ icaoCode + "already exists.");});

        Subscription subscription = new Subscription();
        subscription.setIcaoCode(subscriptionDto.icaoCode());
        subscriptionRepository.save(subscription);
        return new SubscriptionDto(subscription.getIcaoCode());

    }

    public SubscriptionDto deleteSubscription(String icaoCode) {
        Subscription deleteSubscription = subscriptionRepository.findByIcaoCode(icaoCode.toUpperCase()).orElseThrow(()-> new IllegalArgumentException("Subscription not found for: " + icaoCode));
        subscriptionRepository.delete(deleteSubscription);
        return new SubscriptionDto(deleteSubscription.getIcaoCode());
    }

    public List<SubscriptionDto> getSubscriptions() {
        return subscriptionRepository.findAll().stream().map(subscription -> new SubscriptionDto(subscription.getIcaoCode())).collect(Collectors.toList());
    }
}
