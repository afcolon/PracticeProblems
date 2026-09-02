package com.example.api.service;

import java.util.List;
import com.example.api.model.Subscription;
import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;
import com.example.api.dto.SubscriptionUpdateDto;
import com.example.api.dto.SubscriptionItemDto;

public interface SubscriptionService {

    /**
     * Creates a new subscription. throws if the email address is invalid or has 
     * already been subscribed.
     */
    SubscriptionResponse createSubscription(SubscriptionRequest request);

    /**
     * Returns a list of all subscriptions
     */
    List<Subscription> getAllSubscriptions();

    /**
     * Returns a list of all subscriptions as {@code SubscriptionItemDto}s
     */
    List<SubscriptionItemDto> getAllSubscriptionDtos();

    /**
     * Updates a subscription based on the provided id and {@code SubscriptionUpdateDto}.
     * Throws if id is invalid or new subscription information is invalid.
     */
    SubscriptionResponse updateSubscription(long id, SubscriptionUpdateDto dto);
}
