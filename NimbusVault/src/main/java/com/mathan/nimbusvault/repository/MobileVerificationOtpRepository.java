package com.mathan.nimbusvault.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mathan.nimbusvault.entity.MobileVerificationOtp;

public interface MobileVerificationOtpRepository
        extends JpaRepository<MobileVerificationOtp, UUID> {

    Optional<MobileVerificationOtp> findByOtpAndUserId(String otp, UUID userId);
}