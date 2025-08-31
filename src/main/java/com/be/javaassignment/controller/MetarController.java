package com.be.javaassignment.controller;

import com.be.javaassignment.dto.MetarDto;
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
    public ResponseEntity<MetarDto> getMetarData(@PathVariable String icaoCode){
        log.info("Received GET request for METAR data of airport: {}", icaoCode);
        return ResponseEntity.ok(metarService.getMetarData(icaoCode));
    }

    @PostMapping
    public ResponseEntity<MetarDto> addMetarData(@PathVariable String icaoCode, @RequestBody MetarDto metarDto){
        log.info("Received POST request to store METAR data for airport: {}", icaoCode);
        return ResponseEntity.ok(metarService.addMetarData(icaoCode, metarDto));
    }

}


