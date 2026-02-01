package com.rsch.dto;

public record AuthenticationRequest(
        String username,
        String password
) {}