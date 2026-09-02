package com.example.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.dto.SubscriptionItemDto;
import com.example.api.dto.SubscriptionUpdateDto;
import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;
import com.example.api.service.SubscriptionService;

@RestController
@RequestMapping("/api")
public class SubscriptionController {
    
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Creates a new subscription.
     */
    @PostMapping("/subscriptions")
    public ResponseEntity<SubscriptionResponse> createSubscription(
        @RequestBody SubscriptionRequest request
    ) {
        SubscriptionResponse response = subscriptionService.createSubscription(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Gets all subscriptions
     */
    @GetMapping("/subscriptions")
    public ResponseEntity<List<SubscriptionItemDto>> getAllSubscriptions() {
        List<SubscriptionItemDto> subscriptions = subscriptionService.getAllSubscriptionDtos();
        return ResponseEntity.ok(subscriptions);
    }
    
    /**
     * Updates a subscription given an id and {@code SubscriptionUpdateDto} payload with the new data
     */
    @PutMapping("/subscriptions/{id}")
    public ResponseEntity<SubscriptionResponse> updateSubscription(
        @PathVariable long id,
        @RequestBody SubscriptionUpdateDto dto
    ) {
        SubscriptionResponse response = subscriptionService.updateSubscription(id, dto);
        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a subscription given an id
     */
    @DeleteMapping("/subscriptions/{id}")
    public ResponseEntity<SubscriptionResponse> deleteSubscription(
        @PathVariable long id
    ) {
        SubscriptionResponse response = subscriptionService.deleteSubscription(id);
        return ResponseEntity.ok(response);
    }
}
