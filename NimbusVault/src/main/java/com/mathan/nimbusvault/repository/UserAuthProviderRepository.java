package com.mathan.nimbusvault.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mathan.nimbusvault.entity.UserAuthProvider;
import com.mathan.nimbusvault.enums.AuthProvider;

public interface UserAuthProviderRepository
        extends JpaRepository<UserAuthProvider, UUID> {

    Optional<UserAuthProvider> findByProviderAndProviderId(
            AuthProvider provider, String providerId);
}