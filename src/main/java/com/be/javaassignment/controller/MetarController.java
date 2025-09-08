package com.be.javaassignment.controller;

import com.be.javaassignment.dto.metar.MetarRequestDto;
import com.be.javaassignment.dto.metar.MetarRequestFilterDto;
import com.be.javaassignment.dto.metar.MetarResponseDto;
import com.be.javaassignment.service.MetarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airport/{icaoCode}/METAR")
@AllArgsConstructor
@Slf4j
public class MetarController {

    private final MetarService metarService;

    @GetMapping
    public ResponseEntity<MetarResponseDto> getMetarData(@PathVariable String icaoCode){
        log.info("Received GET request for METAR data of airport with ICAO code {}", icaoCode);
        return ResponseEntity.ok(metarService.getMetarData(icaoCode));
    }

    @PostMapping
    public ResponseEntity<MetarResponseDto> addMetarData(@PathVariable String icaoCode, @RequestBody MetarRequestDto metarRequestDto){
        log.info("Received POST request to store METAR data for airport with ICAO code {}", icaoCode);
        return ResponseEntity.ok(metarService.addMetarData(icaoCode, metarRequestDto));
    }

    @PostMapping("/filter")
    public ResponseEntity<MetarResponseDto> getFilteredMetarData(@PathVariable String icaoCode,@RequestBody MetarRequestFilterDto filter){
        log.info("Received POST request for filtered METAR data for airport with ICAO code {}", icaoCode);
        return ResponseEntity.ok(metarService.getFilteredMetarData(icaoCode,filter));
    }

}


