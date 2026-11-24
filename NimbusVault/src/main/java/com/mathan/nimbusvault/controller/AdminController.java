package com.mathan.nimbusvault.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;

import com.mathan.nimbusvault.dto.response.AdminUserResponse;
import com.mathan.nimbusvault.service.AdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminDashboard() {
        return "Welcome to NimbusVault Admin Dashboard!";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminUserResponse> getAllUsers() {
        return adminService.getAllUsers();
    }
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public AdminUserResponse getUserById(@PathVariable UUID userId) {

        return adminService.getUserById(userId);
    }
    @PutMapping("/users/{userId}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public String disableUser(@PathVariable UUID userId) {

        return adminService.disableUser(userId);
    }
    
    @PutMapping("/users/{userId}/enable")
    @PreAuthorize("hasRole('ADMIN')")
    public String enableUser(@PathVariable UUID userId) {

        return adminService.enableUser(userId);
    }
}