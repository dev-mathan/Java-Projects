package com.mathan.nimbusvault.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mathan.nimbusvault.dto.response.UserResponse;
import com.mathan.nimbusvault.entity.User;
import com.mathan.nimbusvault.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {

        User user = userService.getCurrentUser();

        UserResponse response = new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.getEmailVerified(),
                user.getStorageUsed(),
                user.getStorageLimit()
        );

        return ResponseEntity.ok(response);
    }
}