package com.mathan.nimbusvault.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.mathan.nimbusvault.exception.InvalidCredentialsException;
import com.mathan.nimbusvault.dto.request.RefreshTokenRequest;
import com.mathan.nimbusvault.dto.request.LoginRequest;
import com.mathan.nimbusvault.dto.response.AuthResponse;
import com.mathan.nimbusvault.entity.RefreshToken;
import org.springframework.stereotype.Service;

import com.mathan.nimbusvault.dto.request.RegisterRequest;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.enums.AuthProvider;
import com.mathan.nimbusvault.enums.Role;
import com.mathan.nimbusvault.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JwtService jwtService;

    private final RefreshTokenService refreshTokenService;

    @Transactional
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
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

        // 4. Save user
        userRepository.save(user);
    }
  
    @Transactional
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!Boolean.TRUE.equals(user.getEnabled())) {
            throw new RuntimeException("Account is disabled");
        }

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {
        	throw new InvalidCredentialsException("Invalid email or password");
        }

        String accessToken = jwtService.generateToken(user.getEmail());

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                refreshToken.getToken(),
                "Bearer"
        );
    }
    @Transactional
    public AuthResponse refreshAccessToken(RefreshTokenRequest request) {

        RefreshToken oldToken =
                refreshTokenService.verifyRefreshToken(request.getRefreshToken());

        User user = oldToken.getUser();

        refreshTokenService.revokeRefreshToken(oldToken);

        String accessToken = jwtService.generateToken(user.getEmail());

        RefreshToken newRefreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                newRefreshToken.getToken(),
                "Bearer"
        );
    }
}