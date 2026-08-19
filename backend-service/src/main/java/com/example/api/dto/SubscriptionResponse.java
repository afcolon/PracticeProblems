package com.example.api.dto;

public class SubscriptionResponse {
    private final String message;

    public SubscriptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
}
