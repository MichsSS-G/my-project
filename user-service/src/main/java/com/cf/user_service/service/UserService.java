package com.cf.user_service.service;

import com.cf.user_service.exception.UserAlreadyExistsException;
import com.cf.user_service.exception.UserNotFoundException;
import com.cf.user_service.repository.UserRepository;
import com.cf.user_service.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final String duplicateEmailMessage = "User with this email already exists";

    private User getUserByIdOrThrow(Long id) {

        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User with this id doesn't exist");
        }
        return optionalUser.get();
    }

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(duplicateEmailMessage);
        }
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return getUserByIdOrThrow(id);
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(getUserByIdOrThrow(id));
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User user = getUserByIdOrThrow(id);
        if (!user.getEmail().equals(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new UserAlreadyExistsException(duplicateEmailMessage);
        }
        user.setName(updatedUser.getName());
        user.setSurname(updatedUser.getSurname());
        user.setEmail(updatedUser.getEmail());
        userRepository.save(user);
        return user;
    }

    @Transactional
    public User patchUser(Long id, User patchedUser) {
        User user = getUserByIdOrThrow(id);
        if (patchedUser.getName() != null) {
            user.setName(patchedUser.getName());
        }
        if (patchedUser.getSurname() != null) {
            user.setSurname(patchedUser.getSurname());
        }
        if (patchedUser.getEmail() != null) {
            if (!user.getEmail().equals(patchedUser.getEmail()) && userRepository.existsByEmail(patchedUser.getEmail())) {
                throw new UserAlreadyExistsException(duplicateEmailMessage);
            }
            user.setEmail(patchedUser.getEmail());
        }
        return userRepository.save(user);
    }
}
