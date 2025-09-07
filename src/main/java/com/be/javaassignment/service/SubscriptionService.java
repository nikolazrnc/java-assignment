package com.be.javaassignment.service;

import com.be.javaassignment.dto.subscription.SubscriptionFilterDto;
import com.be.javaassignment.dto.subscription.SubscriptionRequestDto;
import com.be.javaassignment.dto.subscription.SubscriptionResponseDto;
import com.be.javaassignment.dto.subscription.SubscriptionStatusDto;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponseDto addSubscription(SubscriptionRequestDto subscriptionRequestDto);
    SubscriptionResponseDto deleteSubscription(String icaoCode);
    List<SubscriptionResponseDto> getSubscriptions(SubscriptionFilterDto filter);
    SubscriptionResponseDto updateSubscriptionStatus(String icaoCode, SubscriptionStatusDto statusDto);
}
