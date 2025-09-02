package com.be.javaassignment.mapper;

import com.be.javaassignment.dto.MetarDto;
import com.be.javaassignment.model.Metar;
import com.be.javaassignment.model.Subscription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MetarMapper {

    @Mapping(target = "metarId", source = "metarId")
    @Mapping(target = "icaoCode", source = "subscription.icaoCode")
    MetarDto toDto(Metar metar);

    @Mapping(target = "metarId", ignore = true)
    @Mapping(source = "subscription", target = "subscription")
    Metar toEntity(MetarDto metarDto, Subscription subscription);
}

