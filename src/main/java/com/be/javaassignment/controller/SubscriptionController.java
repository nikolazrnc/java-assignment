package com.be.javaassignment.controller;

import com.be.javaassignment.dto.SubscriptionDto;
import com.be.javaassignment.service.SubscriptionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscriptions")
@AllArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getSubscriptions() {
        return ResponseEntity.ok(subscriptionService.getSubscriptions());
    }

    @PostMapping
    public ResponseEntity<SubscriptionDto> addSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        SubscriptionDto addedSubscription=subscriptionService.addSubscription(subscriptionDto);
        return ResponseEntity.ok(addedSubscription);

    }

    @DeleteMapping("/{icaoCode}")
    public ResponseEntity<SubscriptionDto> deleteSubscription(@PathVariable String icaoCode){
        SubscriptionDto deletedSubscription= subscriptionService.deleteSubscription(icaoCode);
        return ResponseEntity.ok(deletedSubscription);
    }
}
