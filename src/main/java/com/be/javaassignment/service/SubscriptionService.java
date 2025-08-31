package com.be.javaassignment.service;

import com.be.javaassignment.dto.SubscriptionDto;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDto addSubscription(SubscriptionDto subscriptionDto);
    SubscriptionDto deleteSubscription(String icaoCode);
    List<SubscriptionDto> getSubscriptions();
}
