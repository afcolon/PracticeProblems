package com.example.api.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;
import com.example.api.model.Subscription;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    
        // In memory store - swap for real repo later if you add a Db
        private final Map<String, Subscription> subscriptions = new ConcurrentHashMap<>();
        private AtomicLong idIncrement = new AtomicLong(1);

        @Override
        public SubscriptionResponse createSubscription(SubscriptionRequest request) {
            String email = request.getEmail();
            System.out.println("Microservices received subscription:" + email);

            //uniqueness check
            boolean emailExists = subscriptions.values().stream().anyMatch(subscription -> subscription.getEmail().equalsIgnoreCase(email));
            if (emailExists) {
                return new SubscriptionResponse("BAD REQUEST", "Email already subscribed.");
            }

            long nextId = idIncrement.getAndIncrement();
            subscriptions.put(Long.toString(nextId), new Subscription(nextId, email, Instant.now()));

            return new SubscriptionResponse("SUCCESS", "Subscription processed: " + email);
        }
}
