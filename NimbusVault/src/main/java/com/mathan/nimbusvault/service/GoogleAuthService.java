
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

        // 4. Check whether this Google account is already linked
        Optional<UserAuthProvider> existingProvider =
                userAuthProviderRepository.findByProviderAndProviderId(
                        AuthProvider.GOOGLE, googleId);

        User user;

        if (existingProvider.isPresent()) {

            // Use the existing linked account
            user = existingProvider.get().getUser();

        } else {

            // 5. Check whether a NimbusVault account already exists
            Optional<User> existingUser = userRepository.findByEmail(email);

            if (existingUser.isPresent()) {
                throw new IllegalArgumentException(
                        "Account already exists. Please use OTP verification to link Google.");
            }

            // 6. Create a new Google account
            user = createGoogleUser(name, email);

            // 7. Save Google provider details
            UserAuthProvider provider = new UserAuthProvider();

            provider.setUser(user);
            provider.setProvider(AuthProvider.GOOGLE);
            provider.setProviderId(googleId);

            userAuthProviderRepository.save(provider);
        }

        // 8. Check whether account is enabled
        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new IllegalArgumentException("Account is disabled");
        }

        // 9. Generate access token
        String accessToken = jwtService.generateToken(user.getEmail());

        // 10. Generate refresh token
        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        // 11. Return authentication response
        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }

    // Create a new Google user
    private User createGoogleUser(String name, String email) {

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setAuthProvider(AuthProvider.GOOGLE);
        user.setRole(Role.USER);
        user.setEmailVerified(true);
        user.setEnabled(true);

        return userRepository.save(user);
    }
}
