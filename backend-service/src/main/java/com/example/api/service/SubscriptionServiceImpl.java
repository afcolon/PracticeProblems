package com.example.api.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.example.api.dto.SubscriptionItemDto;
import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;
import com.example.api.dto.SubscriptionUpdateDto;
import com.example.api.exceptions.DuplicateException;
import com.example.api.exceptions.InvalidEmailException;
import com.example.api.exceptions.NotFoundException;
import com.example.api.model.Subscription;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    
        // In memory store - swap for real repo later if you add a Db
        private final Map<Long, Subscription> subscriptions = new ConcurrentHashMap<>();
        private final Set<String> usedEmails = ConcurrentHashMap.newKeySet();

        private final AtomicLong idIncrement = new AtomicLong(1);

        private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        @Override
        public SubscriptionResponse createSubscription(SubscriptionRequest request) {
            String email = request.getEmail();

            // email validation
            if (email == null || email.trim().isEmpty() || !email.matches(EMAIL_REGEX)) {
                throw new InvalidEmailException("Invalid email address");
            }
            // uniqueness check
            if (!usedEmails.add(email.toLowerCase())) {
                throw new DuplicateException("Email already subscribed");
            }
            
            long nextId = idIncrement.getAndIncrement();
            subscriptions.put(nextId, new Subscription(nextId, email, Instant.now()));

            return new SubscriptionResponse("Subscription processed: " + email);
        }

        @Override
        public List<Subscription> getAllSubscriptions() {
            return subscriptions.values().stream().toList();
        }

        @Override
        public List<SubscriptionItemDto> getAllSubscriptionDtos() {
            return getAllSubscriptions().stream().map(SubscriptionItemDto::new).toList();
        }

        @Override
        public SubscriptionResponse updateSubscription(long id, SubscriptionUpdateDto dto) {
            // Validate dto data
            String newEmail = dto.getNewEmail();
            if (newEmail == null || newEmail.trim().isEmpty()) {
                throw new InvalidEmailException("Invalid email address");
            }
            if (!newEmail.matches(EMAIL_REGEX)){
                throw new InvalidEmailException("Invalid email address");
            }

            // Find subscription
            Subscription subscriptionToUpdate = subscriptions.get(id);
            if (subscriptionToUpdate == null) {
                throw new NotFoundException("Invalid id used");
            }

            // No-Op
            String oldEmail = subscriptionToUpdate.getEmail();
            if (oldEmail.equalsIgnoreCase(newEmail)){
                return new SubscriptionResponse("Subscription updated: " + newEmail);
            }

            if(!usedEmails.add(newEmail.toLowerCase())) {
                throw new DuplicateException("New email is already subscribed");
            }

            subscriptionToUpdate.setEmail(newEmail);
            subscriptions.put(subscriptionToUpdate.getId(), subscriptionToUpdate);
            usedEmails.remove(oldEmail.toLowerCase());

            return new SubscriptionResponse("Subscription updated: " + newEmail);
        }

        @Override
        public SubscriptionResponse deleteSubscription(long id) {
            Subscription subscription = subscriptions.remove(id);

            if(subscription != null) {
                usedEmails.remove(subscription.getEmail().toLowerCase());
            }
            
            return new SubscriptionResponse("Subscription deleted");
        }
}
