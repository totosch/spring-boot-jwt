package com.rsch.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        @NotBlank(message = "username cant be blank")
        String username,

        @NotBlank(message = "password cant be blank")
        String password
) {}