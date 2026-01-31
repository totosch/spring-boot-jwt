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
        User userReal = new User(1, "testUser", "123456");
        UserResponse userDto = new UserResponse(1, "testUser");

        when(userRepository.findAll()).thenReturn(List.of(userReal));

        when(userMapper.toResponse(userReal)).thenReturn(userDto);

        List<UserResponse> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).username());

        verify(userRepository).findAll();
    }

    @Test
    void shouldGetUserById() {
        User userReal = new User(1, "testUser", "123456");
        UserResponse userDto = new UserResponse(1, "testUser");

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
        User userReal = new User(1, "testUser", "123456");

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

        UserRequest request = new UserRequest("user", "123456");
        User userToSave = new User(null, "newUser", "pass123");
        User userSaved = new User(1, "newUser", "pass123");
        UserResponse expectedResponse = new UserResponse(1, "newUser");

        when(userMapper.toEntity(request)).thenReturn(userToSave);
        when(userRepository.save(userToSave)).thenReturn(userSaved);
        when(userMapper.toResponse(userSaved)).thenReturn(expectedResponse);

        UserResponse result = userService.insertUser(request);
        assertEquals(expectedResponse, result);

        verify(userRepository).save(userToSave);
    }


}