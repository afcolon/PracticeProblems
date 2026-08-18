package com.example.api.service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;

import com.example.api.dto.LeadRequest;
import com.example.api.dto.LeadResponse;

@Service
public class LeadServiceImpl implements LeadService{
    
        // In memory store - swap for real repo later if you add a Db
        private final List<String> leads = new CopyOnWriteArrayList<>();

        @Override
        public LeadResponse processLead(LeadRequest request) {
            String email = request.getEmail();
            leads.add(email);
            System.out.println("Microservices received lead:" + email);
            return new LeadResponse("SUCCESS", "Microservice processed: " + email);
        }
}
