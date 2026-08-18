package com.example.api.dto;

public class LeadResponse {
    private final String status;
    private final String message;

    public LeadResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() { return status; }
    public String getMessage() { return message; }
}
