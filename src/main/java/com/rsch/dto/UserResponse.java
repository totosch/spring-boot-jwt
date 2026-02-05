package com.rsch.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "lastName", "username", "email" })
public record UserResponse(
        Integer id,
        String name,
        String lastName,
        String username,
        String email
) {}