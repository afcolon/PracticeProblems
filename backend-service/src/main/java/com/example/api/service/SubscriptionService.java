package com.example.api.service;

import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;

public interface SubscriptionService {
    SubscriptionResponse createSubscription(SubscriptionRequest request);
}
