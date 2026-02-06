package com.rsch.service;

import com.rsch.dto.UserRequest;
import com.rsch.dto.UserResponse;
import com.rsch.exception.UserNotFoundException;
import com.rsch.model.User;
import com.rsch.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldGetAllUsers() {
        User userReal = new User(1, "rsch", "123456", "Rodrigo","Schillaci","test@gmail.com");
        UserResponse userDto = new UserResponse(1, "Rodrigo", "Schillaci", "rsch", "test@gmail.com");

        Pageable pageable = PageRequest.of(0, 10);
        Page<User> userPage = new PageImpl<>(List.of(userReal));

        when(userRepository.findByActiveTrue(pageable)).thenReturn(userPage);
        when(userMapper.toResponse(userReal)).thenReturn(userDto);

        Page<UserResponse> result = userService.getAllUsers(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("rsch", result.getContent().get(0).username());

        verify(userRepository).findByActiveTrue(pageable);
    }

    @Test
    void shouldGetUserById() {
        User userReal = new User(1, "rsch", "123456", "Rodrigo", "Schillaci", "test@gmail.com");
        UserResponse userDto = new UserResponse(1, "Rodrigo", "Schillaci", "rsch", "test@gmail.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(userReal));

        when(userMapper.toResponse(userReal)).thenReturn(userDto);

        UserResponse result = userService.getUserById(1);
        assertEquals(userDto, result);

    }

    @Test
    void shouldThrowErrorWhenUserNotFound() {
         when(userRepository.findById(99)).thenReturn(Optional.empty());

         assertThrows(UserNotFoundException.class, () -> {
             userService.getUserById(99);
         });
         verifyNoInteractions(userMapper);
    }

    @Test
    void shouldDeleteUserById() {
        User userReal = new User(1, "test", "test", "test", "test", "test");

        when(userRepository.existsById(1)).thenReturn(true);
        userService.deleteUserById(1);

        verify(userRepository).deleteById(1);
    }

    @Test
    void shouldThrowErrorWhenDeletingNotExistingUser() {
        when(userRepository.existsById(99)).thenReturn(false);

        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUserById(99);
        });

        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void shouldInsertUser() {

        UserRequest request = new UserRequest("Rodrigo","Schillaci", "rsch", "rodrigoschillaci97@gmail.com", "pass123");
        User userToSave = new User(null, "rsch", "pass123", "Rodrigo", "Schillaci", "rodrigoschillaci97@gmail.com");
        User userSaved = new User(1, "rsch", "pass123", "Rodrigo", "Schillaci", "rodrigoschillaci97@gmail.com");
        UserResponse expectedResponse = new UserResponse(1, "Rodrigo", "Schillaci", "rsch", "rodrigoschillaci97@gmail.com");

        when(userMapper.toEntity(request)).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(userSaved);
        when(userMapper.toResponse(userSaved)).thenReturn(expectedResponse);

        UserResponse result = userService.insertUser(request);
        assertEquals(expectedResponse, result);

        verify(userRepository).save(userToSave);
    }
}