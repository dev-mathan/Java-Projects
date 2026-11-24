package com.mathan.nimbusvault.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mathan.nimbusvault.dto.response.AdminUserResponse;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.repository.UserRepository;
import com.mathan.nimbusvault.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public List<AdminUserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> {

                    AdminUserResponse response = new AdminUserResponse();

                    response.setId(user.getId());
                    response.setName(user.getName());
                    response.setEmail(user.getEmail());
                    response.setRole(user.getRole());
                    response.setEnabled(user.getEnabled());
                    response.setEmailVerified(user.getEmailVerified());
                    response.setMobileVerified(user.getMobileVerified());

                    return response;
                })
                .toList();
    }
    public AdminUserResponse getUserById(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + userId));

        AdminUserResponse response = new AdminUserResponse();

        response.setId(user.getId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setEnabled(user.getEnabled());
        response.setEmailVerified(user.getEmailVerified());
        response.setMobileVerified(user.getMobileVerified());

        return response;
    }
    public String disableUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + userId));

        user.setEnabled(false);

        userRepository.save(user);

        return "User account disabled successfully";
    }
    public String enableUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with ID: " + userId));

        user.setEnabled(true);

        userRepository.save(user);

        return "User account enabled successfully";
    }
}