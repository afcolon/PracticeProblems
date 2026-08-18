package com.example.api.service;

import com.example.api.dto.LeadRequest;
import com.example.api.dto.LeadResponse;

public interface LeadService {
    LeadResponse processLead(LeadRequest request);
}
