package com.example.api.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.api.dto.SubscriptionRequest;
import com.example.api.dto.SubscriptionResponse;
import com.example.api.exceptions.InvalidEmailException;
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
    void createsSubscription_failure() throws Exception {
        when(service.createSubscription(any())).thenThrow(new InvalidEmailException("Invalid email address"));
        SubscriptionRequest requestObject = new SubscriptionRequest("invalidemail.com");
        
        mockMvc.perform(
            post("/api/subscriptions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestObject)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value("Invalid email address"));
    }
}
