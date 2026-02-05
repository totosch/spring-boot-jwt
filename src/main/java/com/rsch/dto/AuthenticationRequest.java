package com.rsch.dto;

public record AuthenticationRequest(
        String name,
        String lastName,
        String username,
        String email,
        String password
) {}