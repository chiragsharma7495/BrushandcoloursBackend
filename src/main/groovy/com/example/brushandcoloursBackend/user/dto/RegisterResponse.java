package com.example.brushandcoloursBackend.user.dto;

import com.example.brushandcoloursBackend.user.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
public class RegisterResponse {

    private String id;

    private String name;

    private String email;

    private Role role;

    private Instant createdAt;
}
