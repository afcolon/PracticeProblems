package com.example.api.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.api.model.Subscription;
import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;
import com.example.api.dto.SubscriptionUpdateDto;
import com.example.api.dto.SubscriptionItemDto;
import com.example.api.exceptions.DuplicateException;
import com.example.api.exceptions.InvalidEmailException;
import com.example.api.exceptions.NotFoundException;

public class SubscriptionServiceImplTest {
    
    private SubscriptionServiceImpl subscriptionService;

    @BeforeEach
    void beforeEach() {
        // Create fresh service instance
        subscriptionService = new SubscriptionServiceImpl();
    }

    @Test
    void createSubscription_success_new_email() {
        SubscriptionRequest request = new SubscriptionRequest("testEmail@email.com");
        SubscriptionResponse response = subscriptionService.createSubscription(request);
        assertTrue(response.getMessage().contains("Subscription processed"));
    }

    @Test
    void createSubscription_fails_duplicate_email() {
        SubscriptionRequest request1 = new SubscriptionRequest("dupe@email.com");
        SubscriptionRequest request2 = new SubscriptionRequest("dupe@email.com");

        // First request should pass
        subscriptionService.createSubscription(request1);

        // Second request should fail, dupe email address
        DuplicateException exception = assertThrows(DuplicateException.class, 
            () -> subscriptionService.createSubscription(request2)
        );
        assertEquals("Email already subscribed", exception.getMessage());
    }

    @Test
    void createSubscription_fails_duplicate_email_case_insensitive() {
        SubscriptionRequest request1 = new SubscriptionRequest("dupe@email.com");
        SubscriptionRequest request2 = new SubscriptionRequest("DUPE@EMAIL.COM");

        // First request should pass
        subscriptionService.createSubscription(request1);

        // Second request should fail, dupe email address
        DuplicateException exception = assertThrows(DuplicateException.class, 
            () -> subscriptionService.createSubscription(request2)
        );
        assertEquals("Email already subscribed", exception.getMessage());
    }

    @Test
    void createSubscription_fails_invalid_email() {
        SubscriptionRequest request = new SubscriptionRequest("invalidEmail");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.createSubscription(request)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void createSubscription_fails_empty_email() {
        SubscriptionRequest request = new SubscriptionRequest("");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.createSubscription(request)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void createSubscription_fails_null_email() {
        SubscriptionRequest request = new SubscriptionRequest(null);

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.createSubscription(request)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void getAllSubscriptions_success() {
        String email1 = "email1@email.com";
        String email2 = "email2@email.com";
        SubscriptionRequest request1 = new SubscriptionRequest(email1);
        SubscriptionRequest request2 = new SubscriptionRequest(email2);

        subscriptionService.createSubscription(request1);
        subscriptionService.createSubscription(request2);

        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        assertEquals(2, subscriptions.size());
        List<String> emails = subscriptions.stream().map(Subscription::getEmail).toList();
        assertTrue(emails.contains(email1));
        assertTrue(emails.contains(email2));
    }

    @Test
    void getAllSubscriptions_success_empty() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        assertEquals(0, subscriptions.size());
    }

    @Test
    void getAllSubscriptionDtos_success() {
        String email1 = "email1@email.com";
        String email2 = "email2@email.com";
        SubscriptionRequest request1 = new SubscriptionRequest(email1);
        SubscriptionRequest request2 = new SubscriptionRequest(email2);

        subscriptionService.createSubscription(request1);
        subscriptionService.createSubscription(request2);

        List<SubscriptionItemDto> subscriptionDtos = subscriptionService.getAllSubscriptionDtos();
        assertEquals(2, subscriptionDtos.size());

        List<String> emails = subscriptionDtos.stream().map(SubscriptionItemDto::getEmail).toList();
        assertTrue(emails.contains(email1));
        assertTrue(emails.contains(email2));
    }

    @Test
    void getAllSubscriptionDtos_success_empty() {
        List<SubscriptionItemDto> subscriptionDtos = subscriptionService.getAllSubscriptionDtos();
        assertEquals(0, subscriptionDtos.size());
    }

    @Test
    void dtoConversion_success() {
        String email1 = "email1@email.com";
        String email2 = "email2@email.com";
        SubscriptionRequest request1 = new SubscriptionRequest(email1);
        SubscriptionRequest request2 = new SubscriptionRequest(email2);

        subscriptionService.createSubscription(request1);
        subscriptionService.createSubscription(request2);

        List<Subscription> fullModels = subscriptionService.getAllSubscriptions();
        List<SubscriptionItemDto> dtos = subscriptionService.getAllSubscriptionDtos();

        for (Subscription s : fullModels) {
            boolean matches = dtos.stream()
                .anyMatch(
                    dto -> dto.getId() == s.getId()
                    && dto.getEmail().equals(s.getEmail())
                );
            assertTrue(matches);
        }
    }

    @Test
    void updateSubscription_success() {
        String email = "email@email.com";
        String newEmail = "newEmail@email.com";

        SubscriptionRequest request1 = new SubscriptionRequest(email);
        subscriptionService.createSubscription(request1);
        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions();
        Subscription subscription = subscriptionList.get(0);

        assertEquals(email, subscription.getEmail());
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto(newEmail);

        SubscriptionResponse response = subscriptionService.updateSubscription(subscription.getId(), dto);
        assertTrue(response.getMessage().contains("Subscription updated"));

        List<Subscription> subscriptionListAfterUpdate = subscriptionService.getAllSubscriptions();
        Subscription subscriptionAfterUpdate = subscriptionListAfterUpdate.get(0);

        assertEquals(newEmail, subscriptionAfterUpdate.getEmail());
        assertEquals(subscription.getId(), subscriptionAfterUpdate.getId());
    }

    @Test
    void updateSubscription_fails_null_email() {
        String email = "email@email.com";

        SubscriptionRequest request1 = new SubscriptionRequest(email);
        subscriptionService.createSubscription(request1);
        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions();
        Subscription subscription = subscriptionList.get(0);

        assertEquals(email, subscription.getEmail());
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto(null);

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.updateSubscription(subscription.getId(), dto)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void updateSubscription_fails_empty_email() {
        String email = "email@email.com";

        SubscriptionRequest request1 = new SubscriptionRequest(email);
        subscriptionService.createSubscription(request1);
        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions();
        Subscription subscription = subscriptionList.get(0);

        assertEquals(email, subscription.getEmail());
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto(" ");

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.updateSubscription(subscription.getId(), dto)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void updateSubscription_fails_invalid_email() {
        String email = "email@email.com";
        String email2 = "invalidEmail";

        SubscriptionRequest request1 = new SubscriptionRequest(email);
        subscriptionService.createSubscription(request1);
        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions();
        Subscription subscription = subscriptionList.get(0);

        assertEquals(email, subscription.getEmail());
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto(email2);

        InvalidEmailException exception = assertThrows(InvalidEmailException.class, 
            () -> subscriptionService.updateSubscription(subscription.getId(), dto)
        );
        assertEquals("Invalid email address", exception.getMessage());
    }

    @Test
    void updateSubscription_fails_invalid_id() {
        String email = "email@email.com";
        String email2 = "validEmail@email.com";
        long invalidId = 99999;

        SubscriptionRequest request1 = new SubscriptionRequest(email);
        subscriptionService.createSubscription(request1);
        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions();
        Subscription subscription = subscriptionList.get(0);

        assertEquals(email, subscription.getEmail());
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto(email2);

        NotFoundException exception = assertThrows(NotFoundException.class, 
            () -> subscriptionService.updateSubscription(invalidId, dto)
        );
        assertEquals("Invalid id used", exception.getMessage());
    }

    @Test
    void updateSubscription_fails_dupe_email_ignores_case() {
        String email1 = "email@email.com";
        String email2 = "validEmail@email.com";
        String dupeEmail2 = "VALIDEmail@email.com";

        SubscriptionRequest request1 = new SubscriptionRequest(email1);
        subscriptionService.createSubscription(request1);
        SubscriptionRequest request2 = new SubscriptionRequest(email2);
        subscriptionService.createSubscription(request2);

        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions();
        Map<String, Subscription> subscriptionListByEmail = subscriptionList.stream().collect(Collectors.toMap(Subscription::getEmail, Function.identity()));
        Subscription subscription1 = subscriptionListByEmail.get(email1);
        Subscription subscription2 = subscriptionListByEmail.get(email2);
        assertEquals(email1, subscription1.getEmail());
        assertEquals(email2, subscription2.getEmail());

        SubscriptionUpdateDto dto = new SubscriptionUpdateDto(dupeEmail2);
        DuplicateException exception = assertThrows(DuplicateException.class, 
            () -> subscriptionService.updateSubscription(subscription1.getId(), dto)
        );
        assertEquals("New email is already subscribed", exception.getMessage());
        
        List<Subscription> subscriptionListAfterNoOp = subscriptionService.getAllSubscriptions();
        Map<String, Subscription> subscriptionListByEmailAfterNoOp = subscriptionListAfterNoOp.stream().collect(Collectors.toMap(Subscription::getEmail, Function.identity()));
        Subscription subscription1AfterNoOp = subscriptionListByEmailAfterNoOp.get(email1);
        Subscription subscription2AfterNoOP = subscriptionListByEmailAfterNoOp.get(email2);
        assertEquals(email1, subscription1AfterNoOp.getEmail());
        assertEquals(email2, subscription2AfterNoOP.getEmail());
    }

    @Test
    void updateSubscription_succeeds_no_changes_no_op() {
        String email1 = "email@email.com";
        String email2 = "validEmail@email.com";
        String dupeEmail2 = "VALIDEmail@email.com";

        SubscriptionRequest request1 = new SubscriptionRequest(email1);
        subscriptionService.createSubscription(request1);
        SubscriptionRequest request2 = new SubscriptionRequest(email2);
        subscriptionService.createSubscription(request2);

        List<Subscription> subscriptionList = subscriptionService.getAllSubscriptions();
        Map<String, Subscription> subscriptionListByEmail = subscriptionList.stream().collect(Collectors.toMap(Subscription::getEmail, Function.identity()));
        Subscription subscription1 = subscriptionListByEmail.get(email1);
        Subscription subscription2 = subscriptionListByEmail.get(email2);
        assertEquals(email1, subscription1.getEmail());
        assertEquals(email2, subscription2.getEmail());

        SubscriptionUpdateDto dto = new SubscriptionUpdateDto(dupeEmail2);
        SubscriptionResponse response = subscriptionService.updateSubscription(subscription2.getId(), dto);
        assertTrue(response.getMessage().contains("Subscription updated"));

        List<Subscription> subscriptionListAfterNoOp = subscriptionService.getAllSubscriptions();
        Map<String, Subscription> subscriptionListByEmailAfterNoOp = subscriptionListAfterNoOp.stream().collect(Collectors.toMap(Subscription::getEmail, Function.identity()));
        Subscription subscription1AfterNoOp = subscriptionListByEmailAfterNoOp.get(email1);
        Subscription subscription2AfterNoOP = subscriptionListByEmailAfterNoOp.get(email2);
        assertEquals(email1, subscription1AfterNoOp.getEmail());
        assertEquals(email2, subscription2AfterNoOP.getEmail());
    }
}
