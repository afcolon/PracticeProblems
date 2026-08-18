package com.example.api.model;

import java.time.Instant;

public class Subscription {
    private final long id;

    private String email;

    private final Instant createdAt;

    
    public Subscription(long id, String email, Instant createdAt){
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
    }

    public long getId() { return this.id; }
    public String getEmail() { return this.email; }
    public Instant getCreatedAt() { return this.createdAt; }

    public void setEmail(String email) { this.email = email; }

}
