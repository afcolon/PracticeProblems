package com.example.api.dto;

/**
 * Payload from client with new data to update a subscription
 */
public class SubscriptionUpdateDto {
    private String newEmail;


    public SubscriptionUpdateDto() {}

    public SubscriptionUpdateDto(String email) {
        this.newEmail = email;
    }

    public String getNewEmail() { return this.newEmail; }

    public void setNewEmail(String newEmail) { this.newEmail = newEmail; }
}
