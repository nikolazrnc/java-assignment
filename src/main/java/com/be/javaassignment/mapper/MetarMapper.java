package com.be.javaassignment.mapper;

import com.be.javaassignment.dto.metar.MetarRequestDto;
import com.be.javaassignment.dto.metar.MetarResponseDto;
import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetarMapper {

    @Mapping(source = "subscription.subscriptionId", target = "subscriptionId")
    MetarResponseDto toResponseDto(Metar metar);
    @Mapping(source = "subscription", target = "subscription")
    Metar toEntity(MetarRequestDto metarRequestDto, Subscription subscription);
}

