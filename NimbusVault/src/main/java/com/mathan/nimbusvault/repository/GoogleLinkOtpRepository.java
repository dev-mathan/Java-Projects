
package com.mathan.nimbusvault.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mathan.nimbusvault.entity.GoogleLinkOtp;

public interface GoogleLinkOtpRepository extends JpaRepository<GoogleLinkOtp, UUID> {

Optional<GoogleLinkOtp> findFirstByUserIdAndGoogleIdAndOtpAndUsedFalseOrderByCreatedAtDesc(
        UUID userId,
        String googleId,
        String otp);

}
