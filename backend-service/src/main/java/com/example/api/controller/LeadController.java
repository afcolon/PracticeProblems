package com.example.api.controller;

import com.example.api.dto.LeadRequest;
import com.example.api.dto.LeadResponse;
import com.example.api.service.LeadService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LeadController {
    
    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping("/leads")
    public LeadResponse processLead(
        @RequestBody LeadRequest request
    ) {
        return leadService.processLead(request);
    }
}
