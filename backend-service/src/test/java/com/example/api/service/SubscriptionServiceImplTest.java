package com.example.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;
import com.example.api.exceptions.DuplicateException;
import com.example.api.exceptions.InvalidEmailException;

public class SubscriptionServiceImplTest {
    
    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void beforeEach() {
        // Create fresh service instance
        subscriptionService = new SubscriptionServiceImpl();
    }

    @Test
    void createSubscription_success_new_email() {
        SubscriptionRequest request = new SubscriptionRequest("testEmail@email.com");
        SubscriptionResponse response = subscriptionService.createSubscription(request);
        assertTrue(response.getMessage().contains("Subscription processed"));
    }

    @Test
    void createSubscription_fails_duplicate_email() {
        SubscriptionRequest request1 = new SubscriptionRequest("dupe@email.com");
        SubscriptionRequest request2 = new SubscriptionRequest("dupe@email.com");

        // First request should pass
        subscriptionService.createSubscription(request1);

        // Second request should fail, dupe email address
        DuplicateException exception = assertThrows(DuplicateException.class, 
            () -> subscriptionService.createSubscription(request2)
        );
        assertEquals("Email already subscribed", exception.getMessage());
    }

    @Test
    void createSubscription_fails_duplicate_email_case_insensitive() {
        SubscriptionRequest request1 = new SubscriptionRequest("dupe@email.com");
        SubscriptionRequest request2 = new SubscriptionRequest("DUPE@EMAIL.COM");

        // First request should pass
        subscriptionService.createSubscription(request1);

        // Second request should fail, dupe email address
        DuplicateException exception = assertThrows(DuplicateException.class, 
            () -> subscriptionService.createSubscription(request2)
        );
        assertEquals("Email already subscribed", exception.getMessage());
    }

    @Test
    void createSubscription_fails_invalid_email() {
        SubscriptionRequest request = new SubscriptionRequest("invalidEmail");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.createSubscription(request)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void createSubscription_fails_empty_email() {
        SubscriptionRequest request = new SubscriptionRequest("");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.createSubscription(request)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void createSubscription_fails_null_email() {
        SubscriptionRequest request = new SubscriptionRequest(null);

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.createSubscription(request)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }
}
