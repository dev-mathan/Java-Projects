
package com.mathan.nimbusvault.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    // Password reset OTP email
    public void sendPasswordResetOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("NimbusVault - Password Reset OTP");

        message.setText(
                "Hello,\n\n" +
                "Your password reset OTP is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\nNimbusVault Team"
        );

        mailSender.send(message);
    }

    // Email verification link
    public void sendEmailVerification(String email, String token) {

        String verificationLink =
                "http://localhost:8080/api/v1/auth/verify-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("NimbusVault - Verify Your Email");

        message.setText(
                "Hello,\n\n" +
                "Please verify your email by clicking the link below:\n\n" +
                verificationLink + "\n\n" +
                "This link is valid for 15 minutes.\n\n" +
                "Regards,\nNimbusVault Team"
        );

        mailSender.send(message);
    }
 // Google account linking OTP email
    public void sendGoogleLinkOtp(String email, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("NimbusVault - Google Account Linking OTP");

        message.setText(
                "Hello,\n\n" +
                "We received a request to link your Google account " +
                "with your NimbusVault account.\n\n" +
                "Your verification OTP is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n\n" +
                "If you did not request this, please ignore this email.\n\n" +
                "Regards,\nNimbusVault Team"
        );

        mailSender.send(message);
    }
}
