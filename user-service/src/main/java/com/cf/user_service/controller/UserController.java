package com.cf.user_service.controller;

import com.cf.user_service.dto.PatchUserDto;
import com.cf.user_service.dto.UserRequestDto;
import com.cf.user_service.dto.UserResponseDto;
import com.cf.user_service.dto.UpdateUserRequestDto;
import com.cf.user_service.entity.User;
import com.cf.user_service.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto dto) {
        User user = mapToEntity(dto);
        User savedUser = userService.createUser(user);
        return mapToDto(savedUser);
    }

    @GetMapping
    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers().stream().map(this::mapToDto).toList();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return mapToDto(userService.getUserById(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserResponseDto updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDto dto) {
        User updatedUser = userService.updateUser(id, mapToEntity(dto));
        return mapToDto(updatedUser);
    }

    @PatchMapping("/{id}")
    public UserResponseDto patchUser(@PathVariable Long id, @RequestBody PatchUserDto dto) {
        User patchedUser = userService.patchUser(id, mapToEntity(dto));
        return mapToDto(patchedUser);
    }

    private UserResponseDto mapToDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getSurname(), user.getEmail());
    }

    private User mapToEntity(UserRequestDto dto) {
        return new User(dto.getName(), dto.getSurname(), dto.getEmail());
    }

    private User mapToEntity(UpdateUserRequestDto dto) {
        return new User(dto.getName(), dto.getSurname(), dto.getEmail());
    }

    private User mapToEntity(PatchUserDto dto) {
        return new User(dto.getName(), dto.getSurname(), dto.getEmail());
    }
}
