
package com.mathan.nimbusvault.service;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.mathan.nimbusvault.entity.GoogleLinkOtp;
import com.mathan.nimbusvault.entity.User;
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

    @Transactional
    public String sendGoogleLinkOtp(String idToken)
            throws GeneralSecurityException, java.io.IOException {

        // 1. Verify Google ID token
        GoogleIdToken.Payload payload =
                googleTokenService.verify(idToken);

        // 2. Get Google account details
        String googleId = payload.getSubject();
        String email = payload.getEmail();

        // 3. Check Google email verification
        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new IllegalArgumentException(
                    "Google email is not verified");
        }

        // 4. Find existing NimbusVault account
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(
                        "NimbusVault account not found"));

        // 5. Check account status
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new IllegalArgumentException("Account is disabled");
        }

        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new IllegalArgumentException(
                    "Please verify your NimbusVault email first");
        }

        // 6. Check whether this Google account is already linked
        Optional<com.mathan.nimbusvault.entity.UserAuthProvider> existing =
                userAuthProviderRepository
                        .findByProviderAndProviderId(
                                AuthProvider.GOOGLE, googleId);

        if (existing.isPresent()) {
            throw new IllegalArgumentException(
                    "This Google account is already linked");
        }

        // 7. Generate 6-digit OTP
        String otp = String.format(
                "%06d", secureRandom.nextInt(1_000_000));

        // 8. Save OTP in database
        GoogleLinkOtp googleLinkOtp = new GoogleLinkOtp();

        googleLinkOtp.setUser(user);
        googleLinkOtp.setGoogleId(googleId);
        googleLinkOtp.setOtp(otp);
        googleLinkOtp.setExpiryDate(
                LocalDateTime.now().plusMinutes(5));

        googleLinkOtpRepository.save(googleLinkOtp);

        // 9. Send OTP to registered email
        mailService.sendGoogleLinkOtp(email, otp);

        return "Google account linking OTP sent successfully";
    }

@Transactional
public String verifyGoogleLinkOtp(String idToken, String otp)
        throws GeneralSecurityException, java.io.IOException {

    // 1. Verify Google ID token
    GoogleIdToken.Payload payload =
            googleTokenService.verify(idToken);

    String googleId = payload.getSubject();
    String email = payload.getEmail();

    if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
        throw new IllegalArgumentException(
                "Google email is not verified");
    }

    // 2. Find the existing NimbusVault user
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException(
                    "NimbusVault account not found"));

    // 3. Find the matching unused OTP
    GoogleLinkOtp linkOtp = googleLinkOtpRepository
            .findFirstByUserIdAndGoogleIdAndOtpAndUsedFalseOrderByCreatedAtDesc(
                    user.getId(), googleId, otp)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Invalid OTP"));

    // 4. Check OTP expiry
    if (linkOtp.getExpiryDate().isBefore(LocalDateTime.now())) {
        throw new IllegalArgumentException("OTP has expired");
    }

    // 5. Check account status
    if (!Boolean.TRUE.equals(user.getEnabled())) {
        throw new IllegalArgumentException("Account is disabled");
    }

    // 6. Link Google account
    com.mathan.nimbusvault.entity.UserAuthProvider provider =
            new com.mathan.nimbusvault.entity.UserAuthProvider();

    provider.setUser(user);
    provider.setProvider(AuthProvider.GOOGLE);
    provider.setProviderId(googleId);

    userAuthProviderRepository.save(provider);

    // 7. Mark OTP as used
    linkOtp.setUsed(true);
    googleLinkOtpRepository.save(linkOtp);

    return "Google account linked successfully";
}

}
