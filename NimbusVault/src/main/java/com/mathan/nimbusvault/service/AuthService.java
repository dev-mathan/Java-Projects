
package com.mathan.nimbusvault.service;

import java.security.SecureRandom;

import java.time.LocalDateTime;
import java.util.List;


import com.mathan.nimbusvault.dto.request.VerifyPasswordResetOtpRequest;
import com.mathan.nimbusvault.dto.request.VerifyMobileRequest;
import java.util.UUID;
import com.mathan.nimbusvault.entity.EmailVerificationToken;
import com.mathan.nimbusvault.repository.EmailVerificationTokenRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mathan.nimbusvault.repository.PasswordResetOtpRepository;
import com.mathan.nimbusvault.dto.request.ChangePasswordRequest;
import com.mathan.nimbusvault.dto.request.CheckEmailRequest;
import com.mathan.nimbusvault.dto.request.ForgotPasswordRequest;
import com.mathan.nimbusvault.dto.request.LoginRequest;
import com.mathan.nimbusvault.dto.request.RefreshTokenRequest;
import com.mathan.nimbusvault.dto.request.RegisterRequest;
import com.mathan.nimbusvault.dto.request.ResendEmailVerificationRequest;
import com.mathan.nimbusvault.dto.response.AuthResponse;
import com.mathan.nimbusvault.entity.PasswordResetOtp;
import com.mathan.nimbusvault.entity.RefreshToken;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.enums.AuthProvider;
import com.mathan.nimbusvault.enums.Role;
import com.mathan.nimbusvault.exception.InvalidCredentialsException;
import com.mathan.nimbusvault.exception.RateLimitExceededException;
import com.mathan.nimbusvault.exception.ResourceNotFoundException;
import com.mathan.nimbusvault.repository.UserRepository;
import com.mathan.nimbusvault.entity.MobileVerificationOtp;
import com.mathan.nimbusvault.dto.request.ResetPasswordRequest;
import com.mathan.nimbusvault.repository.MobileVerificationOtpRepository;
import com.mathan.nimbusvault.dto.request.VerifyMobileOtpRequest;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetOtpRepository passwordResetOtpRepository;
    private final MobileVerificationOtpRepository mobileVerificationOtpRepository;
    private final MailService mailService;
    private final RateLimitService rateLimitService;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    private final SecureRandom secureRandom = new SecureRandom();


    @Transactional
    public void register(RegisterRequest request) {

        // 1. Check duplicate email or mobile number
        if (userRepository.existsByEmail(request.getEmail())
                || userRepository.existsByMobileNumber(request.getMobileNumber())) {

            throw new RuntimeException("Email or mobile number already registered");
        }

        // 2. Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 3. Create User object
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setRole(Role.USER);
        user.setMobileNumber(request.getMobileNumber());
        user.setMobileVerified(false);

        // 4. Save user
        user = userRepository.save(user);

        // 5. Generate email verification token
        String emailToken = UUID.randomUUID().toString();

        // 6. Create email verification token entity
        EmailVerificationToken emailVerificationToken =
                new EmailVerificationToken();

        emailVerificationToken.setToken(emailToken);
        emailVerificationToken.setUser(user);
        emailVerificationToken.setExpiryDate(
                LocalDateTime.now().plusMinutes(15));

        // 7. Save email verification token
        emailVerificationTokenRepository.save(emailVerificationToken);

        // 8. Send verification email
        mailService.sendEmailVerification(user.getEmail(), emailToken);

        // 9. Generate mobile verification OTP
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000));

        // 10. Create MobileVerificationOtp object
        MobileVerificationOtp mobileOtp = new MobileVerificationOtp();

        mobileOtp.setOtp(otp);
        mobileOtp.setUser(user);
        mobileOtp.setExpiryDate(
                LocalDateTime.now().plusMinutes(5));

        // 11. Save mobile OTP
        mobileVerificationOtpRepository.save(mobileOtp);

        // 12. Temporary testing only
        System.out.println("Mobile Verification OTP: " + otp);
    }
    @Transactional
    public AuthResponse login(LoginRequest request) {

        // 1. Check login rate limit
        if (!rateLimitService.allowLoginAttempt()) {
            throw new RateLimitExceededException(
                    "Too many login attempts. Please try again later."
            );
        }

        // 2. Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException(
                                "Invalid email or password"
                        ));

        // 3. Check whether account is enabled
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("Account is disabled");
        }

        // 4. Check whether email is verified
        if (!Boolean.TRUE.equals(user.getEmailVerified())) {
            throw new RuntimeException(
                    "Please verify your email before logging in"
            );
        }

        // 5. Verify password
        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {

            throw new InvalidCredentialsException(
                    "Invalid email or password"
            );
        }

        // 6. Generate access token
        String accessToken =
                jwtService.generateToken(user.getEmail());

        // 7. Generate refresh token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        // 8. Return authentication response
        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }
    @Transactional
    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {

        RefreshToken oldToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        User user = oldToken.getUser();

        refreshTokenService.revokeRefreshToken(oldToken);

        String accessToken = jwtService.generateToken(user.getEmail());

        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                newRefreshToken.getToken(),
                "Bearer"
        );
    }

    @Transactional
    public void logout(RefreshTokenRequest request, String email) {

        RefreshToken refreshToken =
                refreshTokenService.verifyRefreshToken(
                        request.getRefreshToken());

        if (!refreshToken.getUser().getEmail()
                .equalsIgnoreCase(email)) {

            throw new InvalidCredentialsException(
                    "You cannot revoke another user's token");
        }

        refreshTokenService.revokeRefreshToken(refreshToken);
    }

    @Transactional
    public String forgotPassword(ForgotPasswordRequest request) {

        // 1. Check email and mobile number
        User user = userRepository
                .findByEmailAndMobileNumber(
                        request.getEmail(),
                        request.getMobileNumber())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or mobile number"));

        // 2. Check mobile verification
        if (!Boolean.TRUE.equals(user.getMobileVerified())) {
            throw new RuntimeException("Mobile number is not verified");
        }

        // 3. Generate OTP
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000));

        // 4. Create OTP entity
        PasswordResetOtp passwordResetOtp = new PasswordResetOtp();

        passwordResetOtp.setOtp(otp);
        passwordResetOtp.setUser(user);
        passwordResetOtp.setExpiryDate(
                java.time.LocalDateTime.now().plusMinutes(5));

        // 5. Save OTP
        passwordResetOtpRepository.save(passwordResetOtp);

        // Temporary testing only
        System.out.println("Password reset OTP: " + otp);

        return "OTP generated successfully";
    }
    @Transactional(readOnly = true)
    public String checkEmail(CheckEmailRequest request) {

        // Check whether email exists
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Email not registered"));

        return "Email verified. Please enter your mobile number.";
    }
    @Transactional
    public String verifyMobileAndGenerateOtp(VerifyMobileRequest request) {

        // 1. Check rate limit
        if (!rateLimitService.allowForgotPasswordAttempt()) {
            throw new RateLimitExceededException(
                    "Too many password reset attempts. Please try again later."
            );
        }

        // 2. Check email and mobile number belong to the same user
        User user = userRepository
                .findByEmailAndMobileNumber(
                        request.getEmail(),
                        request.getMobileNumber())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or mobile number"));

        // 3. Check mobile verification status
        if (!Boolean.TRUE.equals(user.getMobileVerified())) {
            throw new RuntimeException("Mobile number is not verified");
        }

        // 4. Generate six-digit OTP
        String otp = String.format(
                "%06d",
                secureRandom.nextInt(1_000_000));

        // 5. Create OTP entity
        PasswordResetOtp passwordResetOtp = new PasswordResetOtp();
        passwordResetOtp.setOtp(otp);
        passwordResetOtp.setUser(user);
        passwordResetOtp.setExpiryDate(
                java.time.LocalDateTime.now().plusMinutes(5));

        // 6. Save OTP and send email
        passwordResetOtpRepository.save(passwordResetOtp);
        mailService.sendPasswordResetOtp(user.getEmail(), otp);

        return "OTP generated successfully";
    }

@Transactional
public String verifyMobileOtp(VerifyMobileOtpRequest request) {

    // 1. Find user using email
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // 2. Find OTP belonging to this user
    MobileVerificationOtp mobileOtp = mobileVerificationOtpRepository
            .findByOtpAndUserId(request.getOtp(), user.getId())
            .orElseThrow(() ->
                    new RuntimeException("Invalid OTP"));

    // 3. Check whether OTP is already used
    if (mobileOtp.isUsed()) {
        throw new RuntimeException("OTP already used");
    }

    // 4. Check OTP expiry
    if (mobileOtp.getExpiryDate().isBefore(
            java.time.LocalDateTime.now())) {
        throw new RuntimeException("OTP has expired");
    }

    // 5. Mark OTP as used
    mobileOtp.setUsed(true);

    // 6. Mark user's mobile number as verified
    user.setMobileVerified(true);

    return "Mobile number verified successfully";
}

	@Transactional
	public String verifyPasswordResetOtp(VerifyPasswordResetOtpRequest request) {

    // 1. Find user using email
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // 2. Find OTP belonging to this user
    PasswordResetOtp resetOtp = passwordResetOtpRepository
            .findByOtpAndUserId(request.getOtp(), user.getId())
            .orElseThrow(() ->
                    new RuntimeException("Invalid OTP"));

    // 3. Check whether OTP is already used
    if (resetOtp.isUsed()) {
        throw new RuntimeException("OTP already used");
    }

    // 4. Check OTP expiry
    if (resetOtp.getExpiryDate().isBefore(
            java.time.LocalDateTime.now())) {
        throw new RuntimeException("OTP has expired");
    }

    // 5. Mark OTP as used
    resetOtp.setVerified(true);

    return "Password reset OTP verified successfully";
}

@Transactional
public String resetPassword(ResetPasswordRequest request) {

    // 1. Find user using email
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // 2. Check whether a verified OTP exists
    PasswordResetOtp resetOtp = passwordResetOtpRepository
            .findFirstByUserIdAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(
                    user.getId())
            .orElseThrow(() ->
                    new RuntimeException("Please verify OTP first"));

    // 3. Check OTP expiry
    if (resetOtp.getExpiryDate().isBefore(
            java.time.LocalDateTime.now())) {
        throw new RuntimeException("OTP has expired");
    }

    // 4. Check password confirmation
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
        throw new RuntimeException("Passwords do not match");
    }

    // 5. Encode new password
    String encodedPassword =
            passwordEncoder.encode(request.getNewPassword());

    // 6. Update user password
    user.setPassword(encodedPassword);

    // 7. Mark OTP as used
    resetOtp.setUsed(true);

    return "Password reset successfully";
}
@Transactional
public String changePassword(ChangePasswordRequest request, String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    // 1. Check current password
    if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
        throw new RuntimeException("Current password is incorrect");
    }

    // 2. Check if new password and confirm password match
    if (!request.getNewPassword().equals(request.getConfirmPassword())) {
        throw new RuntimeException("New passwords do not match");
    }

    // 3. Prevent using the same password
    if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
        throw new RuntimeException("New password must be different from current password");
    }

    // 4. Encode and update new password
    user.setPassword(passwordEncoder.encode(request.getNewPassword()));

    userRepository.save(user);

    return "Password changed successfully";
}
@Transactional
public String resendMobileOtp(String email) {

    // 1. Find the registered user
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    // 2. Check whether mobile is already verified
    if (Boolean.TRUE.equals(user.getMobileVerified())) {
        throw new RuntimeException("Mobile number is already verified");
    }

    // 3. Invalidate previous unused OTPs
    List<MobileVerificationOtp> oldOtps =
            mobileVerificationOtpRepository.findByUserIdAndUsedFalse(user.getId());

    for (MobileVerificationOtp oldOtp : oldOtps) {
        oldOtp.setUsed(true);
    }

    // 4. Generate a new six-digit OTP
    String otp = String.format("%06d",
            secureRandom.nextInt(1_000_000));

    // 5. Save the new OTP
    MobileVerificationOtp mobileOtp = new MobileVerificationOtp();
    mobileOtp.setOtp(otp);
    mobileOtp.setUser(user);
    mobileOtp.setExpiryDate(LocalDateTime.now().plusMinutes(5));

    mobileVerificationOtpRepository.save(mobileOtp);

    // 6. Print OTP for local testing
    System.out.println("Mobile Verification OTP: " + otp);

    return "New mobile verification OTP generated successfully";
}
@Transactional
public String verifyEmail(String token) {

    // 1. Find the verification token
    EmailVerificationToken verificationToken =
            emailVerificationTokenRepository.findByToken(token)
                    .orElseThrow(() ->
                            new RuntimeException("Invalid email verification link"));

    // 2. Check whether the token was already used
    if (verificationToken.isUsed()) {
        throw new RuntimeException("Email verification link has already been used");
    }

    // 3. Check token expiry
    if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
        throw new RuntimeException("Email verification link has expired");
    }

    // 4. Mark user's email as verified
    User user = verificationToken.getUser();
    user.setEmailVerified(true);

    // 5. Mark token as used
    verificationToken.setUsed(true);

    return "Email verified successfully";
}
@Transactional
public String resendEmailVerification(ResendEmailVerificationRequest request) {

    // 1. Find user by email
    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new RuntimeException("User not found"));

    // 2. Check whether email is already verified
    if (Boolean.TRUE.equals(user.getEmailVerified())) {
        throw new RuntimeException("Email is already verified");
    }

    // 3. Generate a new verification token
    String emailToken = UUID.randomUUID().toString();

    // 4. Create a new verification token
    EmailVerificationToken verificationToken =
            new EmailVerificationToken();

    verificationToken.setToken(emailToken);
    verificationToken.setUser(user);
    verificationToken.setExpiryDate(
            LocalDateTime.now().plusMinutes(15));

    // 5. Save the token
    emailVerificationTokenRepository.save(verificationToken);

    // 6. Send verification email
    mailService.sendEmailVerification(user.getEmail(), emailToken);

    return "New email verification link sent successfully";
}

}
