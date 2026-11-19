package com.mathan.nimbusvault.service;

import org.springframework.security.crypto.password.PasswordEncoder;
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
  
}