package com.example.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@SpringBootApplication
@RestController
@RequestMapping("/api")
public class ApiController {
    
    public static void main(String[] args) {
        SpringApplication.run(ApiController.class, args);
    }

    @PostMapping("/leads")
    public Map<String, String> processLead(
        @RequestBody Map<String, String> payload
    ) {
        String email = payload.get("email");
        System.out.println("Microservice received lead: " + email);

        Map<String, String> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Microservice processed: " + email);
        return response;
    }
}
