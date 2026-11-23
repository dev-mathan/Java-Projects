
package com.mathan.nimbusvault.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetLink) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("NimbusVault - Password Reset");
        message.setText(
                "Hello,\n\n"
                + "We received a request to reset your password.\n"
                + "Click the link below to reset your password:\n\n"
                + resetLink
                + "\n\nIf you did not request this, please ignore this email."
        );

        mailSender.send(message);
    }
}
