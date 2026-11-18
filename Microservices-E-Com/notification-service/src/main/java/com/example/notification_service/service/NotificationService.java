package com.example.notification_service.service;

import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final EmailService emailService;

    public NotificationService(EmailService emailService) {
        this.emailService = emailService;
    }

    public void processOrderEvent(String message) {
        System.out.println("Processing notification: " + message);

        emailService.sendOrderEmail(message);
    }
}