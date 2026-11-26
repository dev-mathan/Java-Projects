
package com.mathan.nimbusvault.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.mathan.nimbusvault.dto.response.AuthResponse;
import com.mathan.nimbusvault.entity.RefreshToken;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.entity.UserAuthProvider;
import com.mathan.nimbusvault.enums.AuthProvider;
import com.mathan.nimbusvault.enums.Role;
import com.mathan.nimbusvault.repository.UserAuthProviderRepository;
import com.mathan.nimbusvault.repository.UserRepository;

@Service
public class GoogleAuthService {

    private final GoogleTokenService googleTokenService;
    private final UserRepository userRepository;
    private final UserAuthProviderRepository userAuthProviderRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    public GoogleAuthService(
            GoogleTokenService googleTokenService,
            UserRepository userRepository,
            UserAuthProviderRepository userAuthProviderRepository,
            JwtService jwtService,
            RefreshTokenService refreshTokenService) {

        this.googleTokenService = googleTokenService;
        this.userRepository = userRepository;
        this.userAuthProviderRepository = userAuthProviderRepository;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public AuthResponse googleLogin(String idToken)
            throws GeneralSecurityException, IOException {

        // 1. Verify Google ID token
        GoogleIdToken.Payload payload =
                googleTokenService.verify(idToken);

        // 2. Get Google account details
        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        // 3. Check verified email
        if (!Boolean.TRUE.equals(payload.getEmailVerified())) {
            throw new IllegalArgumentException(
                    "Google email is not verified");
        }

        // 4. Check existing Google account
        Optional<UserAuthProvider> existingProvider = userAuthProviderRepository.findByProviderAndProviderId(AuthProvider.GOOGLE, googleId);

        User user;

        if (existingProvider.isPresent()) {

            user = existingProvider.get().getUser();

        } else {

            // 5. Find existing NimbusVault user by email
            user = userRepository.findByEmail(email)
                    .orElseGet(() -> createGoogleUser(name, email, googleId));

            // 6. Link Google account
            UserAuthProvider provider = new UserAuthProvider();

            provider.setUser(user);
            provider.setProvider(AuthProvider.GOOGLE);
            provider.setProviderId(googleId);

            userAuthProviderRepository.save(provider);
        }

        // 7. Generate NimbusVault access token
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new IllegalArgumentException("Account is disabled");
        }
        String accessToken = jwtService.generateToken(user.getEmail());

        // 8. Generate refresh token
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        // 9. Return authentication response
        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }

    private User createGoogleUser(String name, String email, String googleId) {

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setProviderId(googleId);
        user.setRole(Role.USER);
        user.setEmailVerified(true);
        user.setEnabled(true);

        return userRepository.save(user);
    }
}
