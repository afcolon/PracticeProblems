package com.example.api.dto;

import com.example.api.model.Subscription;

/**
 * Payload from server back to client for getting subscriptions
 */
public class SubscriptionItemDto {
    private final long id;

    private final String email;

    public SubscriptionItemDto (long id, String email) {
        this.id = id;
        this.email = email;
    }

    public SubscriptionItemDto (Subscription subscription) {
        this.id = subscription.getId();
        this.email = subscription.getEmail();
    }

    public long getId() { return this.id; }

    public String getEmail() { return this.email; }
}