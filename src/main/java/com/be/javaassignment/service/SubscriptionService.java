package com.be.javaassignment.service;

import com.be.javaassignment.dto.subscription.SubscriptionRequestDto;
import com.be.javaassignment.dto.subscription.SubscriptionResponseDto;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponseDto addSubscription(SubscriptionRequestDto subscriptionRequestDto);
    SubscriptionResponseDto deleteSubscription(String icaoCode);
    List<SubscriptionResponseDto> getSubscriptions();
}
