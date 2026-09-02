package com.example.api.controller;

import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;
import com.example.api.dto.SubscriptionUpdateDto;
import com.example.api.dto.SubscriptionItemDto;
import com.example.api.exceptions.DuplicateException;
import com.example.api.exceptions.InvalidEmailException;
import com.example.api.exceptions.NotFoundException;
import com.example.api.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService service;

    @Test
    void createsSubscription_success() throws Exception {
        when(service.createSubscription(any())).thenReturn(new SubscriptionResponse("Success message"));
        SubscriptionRequest requestObject = new SubscriptionRequest("testEmail@email.com");
        
        mockMvc.perform(
            post("/api/subscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestObject)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Success message"));
    }

    @Test
    void createsSubscription_fails_invalid_email() throws Exception {
        when(service.createSubscription(any())).thenThrow(new InvalidEmailException("Invalid email address"));
        SubscriptionRequest requestObject = new SubscriptionRequest("invalidemail.com");
        
        mockMvc.perform(
            post("/api/subscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestObject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid email address"));
    }

    @Test
    void createsSubscription_fails_duplicate_email() throws Exception {
        when(service.createSubscription(any())).thenThrow(new DuplicateException("Email already subscribed"));
        SubscriptionRequest requestObject = new SubscriptionRequest("duplicateEmail@Email.com");
        
        mockMvc.perform(
            post("/api/subscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestObject)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("Email already subscribed"));
    }

    @Test
    void getSubscriptions_success() throws Exception {
        SubscriptionItemDto dto = new SubscriptionItemDto(1L, "testemail@email.com");
        List<SubscriptionItemDto> subscriptionList = List.of(dto);
        when(service.getAllSubscriptionDtos()).thenReturn(subscriptionList);

        mockMvc.perform(
            get("/api/subscriptions")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].email").value("testemail@email.com"));
    }

    @Test
    void updateSubscription_success() throws Exception {
        when(service.updateSubscription(anyLong(), any())).thenReturn(new SubscriptionResponse("Success message"));
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto("testEmail@email.com");

        mockMvc.perform(
            put("/api/subscriptions/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("Success message"));
    }

    @Test
    void updateSubscription_fails_invalid_email() throws Exception {
        when(service.updateSubscription(anyLong(), any())).thenThrow(new InvalidEmailException("Invalid email address"));
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto("invalidEmail");
        
        mockMvc.perform(
            put("/api/subscriptions/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid email address"));
    }

    @Test
    void updateSubscription_fails_duplicate_email() throws Exception {
        when(service.updateSubscription(anyLong(), any())).thenThrow(new DuplicateException("New email is already subscribed"));
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto("dupeEmail@email.com");
        
        mockMvc.perform(
            put("/api/subscriptions/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.message").value("New email is already subscribed"));
    }

    @Test
    void updateSubscription_fails_invalid_id() throws Exception {
        when(service.updateSubscription(anyLong(), any())).thenThrow(new NotFoundException("Invalid id used"));
        SubscriptionUpdateDto dto = new SubscriptionUpdateDto("validEmail@email.com");
        
        mockMvc.perform(
            put("/api/subscriptions/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Invalid id used"));
    }
}
