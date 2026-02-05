package com.rsch.service;

import com.rsch.dto.UserRequest;
import com.rsch.dto.UserResponse;
import com.rsch.exception.UserNotFoundException;
import com.rsch.model.User;
import com.rsch.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    // Recibe Request DTO, devuelve Response DTO
    public UserResponse insertUser(UserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    public UserResponse getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new UserNotFoundException(id + " not found"));
    }

    public void deleteUserById(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id + " not found");
        }
        userRepository.deleteById(id);
    }
}
