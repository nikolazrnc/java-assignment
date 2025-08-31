package com.be.javaassignment.controller;

import com.be.javaassignment.dto.SubscriptionDto;
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
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions() {
        log.info("Received GET request for all subscriptions");
        return ResponseEntity.ok(subscriptionService.getSubscriptions());
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> addSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        log.info("Received POST request for adding subscription for airport: {}", subscriptionDto.icaoCode());
        SubscriptionDto addedSubscription=subscriptionService.addSubscription(subscriptionDto);
        return ResponseEntity.ok(addedSubscription);

    }

    @DeleteMapping("/{icaoCode}")
    public ResponseEntity<SubscriptionDto> deleteSubscription(@PathVariable String icaoCode){
        log.info("Received DELETE request for deleting subscription for airport: {}", icaoCode);
        SubscriptionDto deletedSubscription= subscriptionService.deleteSubscription(icaoCode);
        return ResponseEntity.ok(deletedSubscription);
    }
}
