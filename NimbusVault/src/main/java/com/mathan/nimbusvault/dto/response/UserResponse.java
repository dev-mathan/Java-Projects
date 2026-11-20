package com.mathan.nimbusvault.dto.response;

import java.util.UUID;

import com.mathan.nimbusvault.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private UUID id;
    private String name;
    private String email;
    private Role role;
    private Boolean emailVerified;
    private Long storageUsed;
    private Long storageLimit;
}