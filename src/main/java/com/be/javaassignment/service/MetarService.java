package com.be.javaassignment.service;

import com.be.javaassignment.dto.metar.MetarRequestDto;
import com.be.javaassignment.dto.metar.MetarRequestFilterDto;
import com.be.javaassignment.dto.metar.MetarResponseDto;

public interface MetarService {

    MetarResponseDto getMetarData(String icaoCode);
    MetarResponseDto addMetarData(String icaoCode, MetarRequestDto metarRequestDto);

    MetarResponseDto getFilteredMetarData(String icaoCode,MetarRequestFilterDto filter);
}
