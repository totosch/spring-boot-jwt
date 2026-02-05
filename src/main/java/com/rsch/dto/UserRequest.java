package com.rsch.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest(

        @NotBlank(message = "name cant be blank")
        String name,

        @NotBlank(message = "lastName cant be blank")
        String lastName,

        @NotBlank(message = "username cant be blank")
        String username,

        @NotBlank(message = "email cant be blank")
        String email,

        @NotBlank(message = "password cant be blank")
        String password
) {}