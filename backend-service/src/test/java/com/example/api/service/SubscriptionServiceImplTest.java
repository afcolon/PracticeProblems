package com.example.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;

public class SubscriptionServiceImplTest {
    
    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void setUp() {
        // Create fresh service instance
        subscriptionService = new SubscriptionServiceImpl();
    }

    @Test
    void createSubscription_success_new_email() {
        SubscriptionRequest request = new SubscriptionRequest("testEmail@email.com");

        SubscriptionResponse response = subscriptionService.createSubscription(request);

        assertEquals("SUCCESS", response.getStatus());
        assertTrue(response.getMessage().contains("Subscription processed"));
    }

    @Test
    void createSubscription_fails_duplicate_email() {
        SubscriptionRequest request1 = new SubscriptionRequest("dupe@email.com");
        SubscriptionRequest request2 = new SubscriptionRequest("dupe@email.com");

        // First request should pass
        subscriptionService.createSubscription(request1);

        // Second request should fail, dupe email address
        SubscriptionResponse response2 = subscriptionService.createSubscription(request2);

        assertEquals("BAD REQUEST", response2.getStatus());
        assertTrue(response2.getMessage().contains("Email already subscribed"));
    }

    @Test
    void createSubscription_fails_invalid_email() {
        SubscriptionRequest request = new SubscriptionRequest("invalidEmail");

        SubscriptionResponse response = subscriptionService.createSubscription(request);

        assertEquals("BAD REQUEST", response.getStatus());
        assertTrue(response.getMessage().contains("Invalid email address"));
    }

    @Test
    void createSubscription_fails_empty_email() {
        SubscriptionRequest request = new SubscriptionRequest("");

        SubscriptionResponse response = subscriptionService.createSubscription(request);

        assertEquals("BAD REQUEST", response.getStatus());
        assertTrue(response.getMessage().contains("Invalid email address"));
    }

    @Test
    void createSubscription_fails_null_email() {
        SubscriptionRequest request = new SubscriptionRequest(null);

        SubscriptionResponse response = subscriptionService.createSubscription(request);

        assertEquals("BAD REQUEST", response.getStatus());
        assertTrue(response.getMessage().contains("Invalid email address"));
    }
}
