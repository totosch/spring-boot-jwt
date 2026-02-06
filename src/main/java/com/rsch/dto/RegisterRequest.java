package com.rsch.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@JsonPropertyOrder({ "name", "lastName", "username", "email", "password" })
public record RegisterRequest(
        @NotBlank(message = "name cant be blank")
        @Size(min = 3, max = 20, message = "name should be between 3 and 20 characters")
        String name,

        @NotBlank(message = "last name cant be blank")
        @Size(min = 3, max = 20, message = "last name should be between 3 and 20 characters")
        String lastName,

        @NotBlank(message = "username cant be blank")
        @Size(min = 3, max = 20, message = "username should be between 3 and 20 characters")
        String username,

        @NotBlank(message = "email cant be blank")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
        String email,

        @NotBlank(message = "password should be between 8 and 20 characters with at least one number and a special character")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+={\\\\[\\\\]};:<>|./?,-]).{8,20}$", message = "password should be between 8 and 20 characters with an uppercase and lowercase letter, at least one number and a special character")
        String password
) {}