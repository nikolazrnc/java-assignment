package com.be.javaassignment.mapper;

import com.be.javaassignment.dto.subscription.SubscriptionResponseDto;
import com.be.javaassignment.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    SubscriptionResponseDto toDto(Subscription subscription);
}
