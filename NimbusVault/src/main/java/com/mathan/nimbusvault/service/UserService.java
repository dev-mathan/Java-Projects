package com.mathan.nimbusvault.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getCurrentUser() {

        UserDetails userDetails = (UserDetails)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        String email = userDetails.getUsername();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}