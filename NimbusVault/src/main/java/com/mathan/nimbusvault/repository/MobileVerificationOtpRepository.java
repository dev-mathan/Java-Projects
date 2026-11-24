package com.mathan.nimbusvault.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mathan.nimbusvault.entity.MobileVerificationOtp;

public interface MobileVerificationOtpRepository
        extends JpaRepository<MobileVerificationOtp, UUID> {

    Optional<MobileVerificationOtp> findByOtpAndUserId(String otp, UUID userId);
    
    List<MobileVerificationOtp> findByUserIdAndUsedFalse(UUID userId);
}