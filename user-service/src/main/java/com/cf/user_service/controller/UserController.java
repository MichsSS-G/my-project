package com.cf.user_service.controller;

import com.cf.user_service.dto.UserPatchRequestDto;
import com.cf.user_service.dto.UserRequestDto;
import com.cf.user_service.dto.UserResponseDto;
import com.cf.user_service.dto.UserUpdateRequestDto;
import com.cf.user_service.entity.User;
import com.cf.user_service.service.UserService;
import com.cf.user_service.mapper.UserMapper;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto dto) {
        User user = userMapper.mapToUser(dto);
        User savedUser = userService.createUser(user);
        return userMapper.mapToDto(savedUser);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream().map(userMapper::mapToDto).toList();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userMapper.mapToDto(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateRequestDto dto) {
        User updatedUser = userService.updateUser(id, userMapper.mapToUser(dto));
        return userMapper.mapToDto(updatedUser);
    }

    @PatchMapping("/{id}")
    public UserResponseDto patchUser(@PathVariable Long id, @Valid @RequestBody UserPatchRequestDto dto) {
        User patchedUser = userService.patchUser(id, userMapper.mapToUser(dto));
        return userMapper.mapToDto(patchedUser);
    }
}
