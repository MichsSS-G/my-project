package com.cf.user_service.mapper;

import com.cf.user_service.dto.UserPatchRequestDto;
import com.cf.user_service.dto.UserRequestDto;
import com.cf.user_service.dto.UserResponseDto;
import com.cf.user_service.dto.UserUpdateRequestDto;
import com.cf.user_service.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponseDto mapToDto(User user) {
        return new UserResponseDto(user.getId(), user.getName(), user.getSurname(), user.getEmail());
    }

    public User mapToUser(UserRequestDto dto) {
        var user = new User();

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());

        return user;
    }

    public User mapToUser(UserUpdateRequestDto dto) {
        var user = new User();

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setEmail(dto.getEmail());

        return user;
    }

    public User mapToUser(UserPatchRequestDto dto) {
        var user = new User();

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getSurname() != null) {
            user.setSurname(dto.getSurname());
        }
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail());
        }

        return user;
    }
}
