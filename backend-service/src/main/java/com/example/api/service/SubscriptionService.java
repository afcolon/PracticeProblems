package com.example.api.service;

import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;

public interface SubscriptionService {

    /**
     * Creates a new subscription. throws if the email address is invalid or has 
     * already been subscribed.
     */
    SubscriptionResponse createSubscription(SubscriptionRequest request);
}
