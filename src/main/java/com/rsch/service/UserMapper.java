package com.rsch.service;

import com.rsch.dto.UserRequest;
import com.rsch.dto.UserResponse;
import com.rsch.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // De DTO a Entidad
    public User toEntity(UserRequest request) {
        User user = new User();
        user.setName(request.name());
        user.setLastName(request.lastName());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        return user;
    }

    // De Entidad a DTO
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail()
        );
    }
}