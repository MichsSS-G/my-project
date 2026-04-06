package com.cf.user_service.controller;

import com.cf.user_service.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.cf.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("POST /users should create a new user")
    void createUserShouldReturnCreatedUser() throws Exception {
        String requestBody = "{\"name\":\"Mikhail\", \"surname\":\"Grib\", \"email\":\"Mikhail@gmail.com\"}";

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Mikhail"))
                .andExpect(jsonPath("$.surname").value("Grib"))
                .andExpect(jsonPath("$.email").value("Mikhail@gmail.com"));

    }

    @Test
    @DisplayName("POST /users with duplicate email should return conflict")
    void createUserShouldReturnConflictWhenEmailAlreadyExists() throws Exception {
        userRepository.save(new User("Mikhail", "Grib", "Mikhail@gmail.com"));

        String requestBody = "{\"name\":\"Egor\", \"surname\":\"Egorov\", \"email\":\"Mikhail@gmail.com\"}";

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User with this email already exists"));
    }

    @Test
    @DisplayName("DELETE /users/id should delete user by id")
    void deleteUserShouldReturnNoContent() throws Exception {

        User saved = userRepository.save(new User("Mikhail", "Grib", "Mikhail@gmail.com"));

        mockMvc.perform(delete("/users/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @BeforeEach
    void clearDB() {
        userRepository.deleteAll();
    }

}
