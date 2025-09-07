package com.be.javaassignment.controller;

import com.be.javaassignment.dto.subscription.SubscriptionFilterDto;
import com.be.javaassignment.dto.subscription.SubscriptionRequestDto;
import com.be.javaassignment.dto.subscription.SubscriptionResponseDto;
import com.be.javaassignment.dto.subscription.SubscriptionStatusDto;
import com.be.javaassignment.service.SubscriptionService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@AllArgsConstructor
@Slf4j
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionResponseDto>> getSubscriptions(@RequestParam(required = false) boolean active, @RequestParam(required = false) String name) {
        log.info("Received GET request for all subscriptions");
        SubscriptionFilterDto filter = new SubscriptionFilterDto(active, name);
        return ResponseEntity.ok(subscriptionService.getSubscriptions(filter));
    }


    @PostMapping
    public ResponseEntity<SubscriptionResponseDto> addSubscription(@RequestBody SubscriptionRequestDto subscriptionRequestDto) {
        log.info("Received POST request for adding subscription for airport with ICAO code {}", subscriptionRequestDto.icaoCode());
        SubscriptionResponseDto addedSubscription=subscriptionService.addSubscription(subscriptionRequestDto);
        return ResponseEntity.ok(addedSubscription);
    }


    @DeleteMapping("/{icaoCode}")
    public ResponseEntity<SubscriptionResponseDto> deleteSubscription(@PathVariable String icaoCode){
        log.info("Received DELETE request for deleting subscription for airport with ICAO code {}", icaoCode);
        SubscriptionResponseDto deletedSubscription= subscriptionService.deleteSubscription(icaoCode);
        return ResponseEntity.ok(deletedSubscription);
    }

    @PutMapping("/{icaoCode}")
    public ResponseEntity<SubscriptionResponseDto> updateSubscriptionStatus(@PathVariable String icaoCode,@RequestBody SubscriptionStatusDto statusDto){
        log.info("Received PUT request for updating subscription status of airport with ICAO code {}", icaoCode);
        SubscriptionResponseDto updatedSubscription= subscriptionService.updateSubscriptionStatus(icaoCode, statusDto);
        return ResponseEntity.ok(updatedSubscription);
    }
}
