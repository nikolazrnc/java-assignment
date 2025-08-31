package com.be.javaassignment.controller;

import com.be.javaassignment.dto.MetarDto;
import com.be.javaassignment.service.MetarService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/airport/{icaoCode}/METAR")
@AllArgsConstructor
public class MetarController {

    private final MetarService metarService;

    @GetMapping
    public ResponseEntity<MetarDto> getMetarData(@PathVariable String icaoCode){
        return ResponseEntity.ok(metarService.getMetarData(icaoCode));
    }

    @PostMapping
    public ResponseEntity<MetarDto> addMetarData(@PathVariable String icaoCode, @RequestBody MetarDto metarDto){
        return ResponseEntity.ok(metarService.addMetarData(icaoCode, metarDto));
    }

}


