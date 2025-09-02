package com.be.javaassignment.mapper;

import com.be.javaassignment.dto.SubscriptionDto;
import com.be.javaassignment.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    @Mapping(source = "subscriptionId", target = "subscriptionId")
    SubscriptionDto toDto(Subscription subscription);
}
