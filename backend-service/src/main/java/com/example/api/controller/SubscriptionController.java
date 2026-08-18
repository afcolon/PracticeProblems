package com.example.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/subscribe")
    public SubscriptionResponse processSubscription(
        @RequestBody SubscriptionRequest request
    ) {
        return subscriptionService.createSubscription(request);
    }
}
