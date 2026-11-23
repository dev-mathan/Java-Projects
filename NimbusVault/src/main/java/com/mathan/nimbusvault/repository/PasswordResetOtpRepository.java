
package com.mathan.nimbusvault.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mathan.nimbusvault.entity.PasswordResetOtp;

public interface PasswordResetOtpRepository
        extends JpaRepository<PasswordResetOtp, UUID> {

    Optional<PasswordResetOtp> findByOtpAndUserId(
            String otp, UUID userId);

    Optional<PasswordResetOtp>
    findFirstByUserIdAndVerifiedTrueAndUsedFalseOrderByCreatedAtDesc(
            UUID userId);
}
