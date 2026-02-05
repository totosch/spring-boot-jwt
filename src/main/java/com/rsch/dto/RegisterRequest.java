package com.rsch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "username cant be blank")
        @Size(min = 3, max = 20, message = "username should be between 3 and 20 characters")
        String username,

        @NotBlank(message = "password cant be blank")
        @Size(min = 6, max = 30, message = "password should be between 6 and 30 characters")
        String password
) {}