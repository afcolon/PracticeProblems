package com.example.api.service;

import java.time.Instant;
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
        private final AtomicLong idIncrement = new AtomicLong(1);

        private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        @Override
        public SubscriptionResponse createSubscription(SubscriptionRequest request) {
            String email = request.getEmail();
            System.out.println("Microservices received subscription:" + email);

            // validation
            if (email == null || email.trim().isEmpty() || !email.matches(EMAIL_REGEX)) {
                return new SubscriptionResponse("BAD REQUEST", "Invalid email address.");
            }
            // uniqueness check
            boolean emailExists = subscriptions.values().stream().anyMatch(subscription -> subscription.getEmail().equalsIgnoreCase(email));
            if (emailExists) {
                return new SubscriptionResponse("BAD REQUEST", "Email already subscribed.");
            }

            long nextId = idIncrement.getAndIncrement();
            subscriptions.put(Long.toString(nextId), new Subscription(nextId, email, Instant.now()));

            return new SubscriptionResponse("SUCCESS", "Subscription processed: " + email);
        }
}
