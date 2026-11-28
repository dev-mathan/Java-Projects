
package com.mathan.nimbusvault.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.mathan.nimbusvault.entity.GoogleLinkOtp;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.entity.UserAuthProvider;
import com.mathan.nimbusvault.enums.AuthProvider;
import com.mathan.nimbusvault.repository.GoogleLinkOtpRepository;
import com.mathan.nimbusvault.repository.UserAuthProviderRepository;
import com.mathan.nimbusvault.repository.UserRepository;

@Service
public class GoogleLinkOtpService {

    private final GoogleTokenService googleTokenService;
    private final GoogleLinkOtpRepository googleLinkOtpRepository;
    private final UserRepository userRepository;
    private final UserAuthProviderRepository userAuthProviderRepository;
    private final MailService mailService;

    private final SecureRandom secureRandom = new SecureRandom();

    public GoogleLinkOtpService(
            GoogleTokenService googleTokenService,
            GoogleLinkOtpRepository googleLinkOtpRepository,
            UserRepository userRepository,
            UserAuthProviderRepository userAuthProviderRepository,
            MailService mailService) {

        this.googleTokenService = googleTokenService;
        this.googleLinkOtpRepository = googleLinkOtpRepository;
        this.userRepository = userRepository;
        this.userAuthProviderRepository = userAuthProviderRepository;
        this.mailService = mailService;
    }

    // Request Google linking OTP
    @Transactional
    public String sendGoogleLinkOtp(String idToken)
            throws GeneralSecurityException, IOException {

        // 1. Verify Google ID token
        GoogleIdToken.Payload payload =
                googleTokenService.verify(idToken);

        String googleId = payload.getSubject();

        // 2. Check Google email verification
        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new IllegalArgumentException(
                    "Google email is not verified");
        }

        // 3. Get authenticated NimbusVault user
        User user = getAuthenticatedUser();

        // 4. Check account type
        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new IllegalArgumentException(
                    "Only LOCAL accounts can link a Google account");
        }

        // 5. Check account status
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new IllegalArgumentException("Account is disabled");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new IllegalArgumentException(
                    "Please verify your NimbusVault email first");
        }

        // 6. Check whether Google account is already linked
        Optional<UserAuthProvider> existing =
                userAuthProviderRepository
                        .findByProviderAndProviderId(
                                AuthProvider.GOOGLE, googleId);

        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "This Google account is already linked");
        }
        // Invalidate all previous OTPs
        List<GoogleLinkOtp> previousOtps =
                googleLinkOtpRepository.findByUserIdAndGoogleIdAndUsedFalse(
                        user.getId(), googleId);

        for (GoogleLinkOtp previousOtp : previousOtps) {
            previousOtp.setUsed(true);
        }

        googleLinkOtpRepository.saveAll(previousOtps);

        // 7. Generate six-digit OTP
        String otp = String.format(
                "%06d", secureRandom.nextInt(1_000_000));

        // 8. Save OTP
        GoogleLinkOtp googleLinkOtp = new GoogleLinkOtp();

        googleLinkOtp.setUser(user);
        googleLinkOtp.setGoogleId(googleId);
        googleLinkOtp.setOtp(otp);
        googleLinkOtp.setExpiryDate(LocalDateTime.now().plusMinutes(5));

        googleLinkOtpRepository.save(googleLinkOtp);

        // 9. Send OTP to authenticated user's email
        mailService.sendGoogleLinkOtp(user.getEmail(), otp);

        return "Google account linking OTP sent successfully";
    }

    // Verify Google linking OTP
    @Transactional
    public String verifyGoogleLinkOtp(String idToken, String otp)
            throws GeneralSecurityException, IOException {

        // 1. Verify Google ID token
        GoogleIdToken.Payload payload =
                googleTokenService.verify(idToken);

        String googleId = payload.getSubject();

        // 2. Check Google email verification
        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new IllegalArgumentException(
                    "Google email is not verified");
        }

        // 3. Get authenticated NimbusVault user
        User user = getAuthenticatedUser();

        // 4. Check account type
        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new IllegalArgumentException(
                    "Only LOCAL accounts can link a Google account");
        }

        // 5. Check account status
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new IllegalArgumentException("Account is disabled");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new IllegalArgumentException(
                    "Please verify your NimbusVault email first");
        }

        // 6. Check whether Google account is already linked
        Optional<UserAuthProvider> existing =
                userAuthProviderRepository
                        .findByProviderAndProviderId(
                                AuthProvider.GOOGLE, googleId);

        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "This Google account is already linked");
        }

        // 7. Find matching unused OTP
        GoogleLinkOtp linkOtp = googleLinkOtpRepository
                .findFirstByUserIdAndGoogleIdAndOtpAndUsedFalseOrderByCreatedAtDesc(
                        user.getId(), googleId, otp)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid OTP"));

        // 8. Check OTP expiry
        if (linkOtp.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("OTP has expired");
        }

        // 9. Link Google account
        UserAuthProvider provider = new UserAuthProvider();

        provider.setUser(user);
        provider.setProvider(AuthProvider.GOOGLE);
        provider.setProviderId(googleId);

        userAuthProviderRepository.save(provider);

        // 10. Mark OTP as used
        linkOtp.setUsed(true);
        googleLinkOtpRepository.save(linkOtp);

        return "Google account linked successfully";
    }

    // Get authenticated NimbusVault user
    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalArgumentException(
                    "Please login to your NimbusVault account first");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Authenticated user not found"));
    }
}
