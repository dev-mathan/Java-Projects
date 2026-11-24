package com.mathan.nimbusvault.dto.response;

import java.util.UUID;

import com.mathan.nimbusvault.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserResponse {

    private UUID id;
    private String name;
    private String email;
    private Role role;
    private Boolean enabled;
    private Boolean emailVerified;
    private Boolean mobileVerified;
}