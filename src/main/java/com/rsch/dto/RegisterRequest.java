package com.rsch.dto;

public record RegisterRequest(
        String username,
        String password
) {}