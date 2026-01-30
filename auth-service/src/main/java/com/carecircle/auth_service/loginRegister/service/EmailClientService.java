package com.carecircle.auth_service.loginRegister.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.carecircle.auth_service.loginRegister.dto.request.WelcomeEmailService;

@Service
public class EmailClientService {

    @Autowired
    private RestTemplate restTemplate;

    @Async
    public void sendWelcomeEmail(WelcomeEmailService request) {

        try {
            restTemplate.postForObject(
                    "http://localhost:8082/api/email/welcome",
                    request,
                    String.class
            );
        } catch (Exception e) {
            // Important: never fail auth flow
            System.out.println("Email service call failed: " + e.getMessage());
        }
    }
}
