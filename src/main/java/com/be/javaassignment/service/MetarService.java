package com.be.javaassignment.service;

import com.be.javaassignment.dto.MetarDto;

public interface MetarService {

    MetarDto getMetarData(String icaoCode);
    MetarDto addMetarData(String icaoCode, MetarDto metarDto);
}
