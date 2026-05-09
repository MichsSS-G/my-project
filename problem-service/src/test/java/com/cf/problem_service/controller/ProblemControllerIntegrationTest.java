package com.cf.problem_service.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.cf.problem_service.repository.ProblemAccessRepository;
import com.cf.problem_service.repository.ProblemRepository;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProblemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProblemAccessRepository problemAccessRepository;

    @BeforeEach
    void setUp() {
        problemAccessRepository.deleteAll();
        problemRepository.deleteAll();
    }

    @Test
    @DisplayName("POST /problems should create problem and return 201")
    void createProblemShouldReturnCreatedProblem() throws Exception {
        Map<String, Object> requestBody = createProblemRequestBody("Two Sum", 1L,
                "EASY", "QUALIFICATION", "SCHOOL");

        mockMvc.perform(post("/problems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Two Sum"))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.generalDifficulty").value("EASY"))
                .andExpect(jsonPath("$.icpcDifficulty").value("QUALIFICATION"))
                .andExpect(jsonPath("$.schoolDifficulty").value("SCHOOL"));
    }

    @Test
    @DisplayName("GET /problems/{id} should return problem when user has access")
    void getProblemByIdShouldReturnProblemWhenUserHasAccess() throws Exception {
        Long problemId = createProblemAndReturnId(1L);

        mockMvc.perform(get("/problems/{id}", problemId)
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(problemId))
                .andExpect(jsonPath("$.title").value("Two Sum"))
                .andExpect(jsonPath("$.ownerId").value(1));
    }

    @Test
    @DisplayName("GET /problems/{id} should return 403 when user has no access")
    void getProblemByIdShouldReturnForbiddenWhenUserHasNoAccess() throws Exception {
        Long problemId = createProblemAndReturnId(1L);

        mockMvc.perform(get("/problems/{id}", problemId)
                        .param("userId", "999"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    @DisplayName("GET /problems should return only problems available to user")
    void getAllProblemsShouldReturnOnlyAvailableProblems() throws Exception {
        createProblemAndReturnId(1L);
        createProblemAndReturnId(2L);

        mockMvc.perform(get("/problems")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ownerId").value(1));
    }

    @Test
    @DisplayName("PATCH /problems/{id} should update problem when user is owner")
    void patchProblemShouldUpdateProblemWhenUserIsOwner() throws Exception {
        Long problemId = createProblemAndReturnId(1L);

        Map<String, Object> requestBody = Map.of(
                "title", "Updated Two Sum",
                "generalDifficulty", "MEDIUM"
        );

        mockMvc.perform(patch("/problems/{id}", problemId)
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Two Sum"))
                .andExpect(jsonPath("$.generalDifficulty").value("MEDIUM"))
                .andExpect(jsonPath("$.schoolDifficulty").value("SCHOOL"))
                .andExpect(jsonPath("$.icpcDifficulty").value("QUALIFICATION"));
    }

    @Test
    @DisplayName("DELETE /problems/{id} should delete problem when user is owner")
    void deleteProblemShouldDeleteProblemWhenUserIsOwner() throws Exception {
        Long problemId = createProblemAndReturnId(1L);

        mockMvc.perform(delete("/problems/{id}", problemId)
                        .param("userId", "1"))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/problems/{id}", problemId)
                        .param("userId", "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET /problems should return 400 when userId is missing")
    void getAllProblemsShouldReturnBadRequestWhenUserIdIsMissing() throws Exception {
        mockMvc.perform(get("/problems"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("POST /problems should return 400 when enum value is invalid")
    void createProblemShouldReturnBadRequestWhenEnumIsInvalid() throws Exception {
        Map<String, Object> requestBody = createProblemRequestBody("Sum", 1L,
                "INVALID", "QUALIFICATION", "SCHOOL");

        mockMvc.perform(post("/problems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("PUT /problems/{id} should fully update problem when user is owner")
    void updateProblemShouldFullyUpdateProblemWhenUserIsOwner() throws Exception {
        Long problemId = createProblemAndReturnId(1L);

        Map<String, Object> requestBody = createProblemRequestBody(
                "Updated Two Sum",
                1L,
                "HARD",
                "QUARTER_FINAL",
                "MUNICIPAL"
        );

        mockMvc.perform(put("/problems/{id}", problemId)
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(problemId))
                .andExpect(jsonPath("$.title").value("Updated Two Sum"))
                .andExpect(jsonPath("$.ownerId").value(1))
                .andExpect(jsonPath("$.generalDifficulty").value("HARD"))
                .andExpect(jsonPath("$.icpcDifficulty").value("QUARTER_FINAL"))
                .andExpect(jsonPath("$.schoolDifficulty").value("MUNICIPAL"));
    }

    @Test
    @DisplayName("PUT /problems/{id} should return 403 when user has no access")
    void updateProblemShouldReturnForbiddenWhenUserHasNoAccess() throws Exception {
        Long problemId = createProblemAndReturnId(1L);

        Map<String, Object> requestBody = createProblemRequestBody(
                "Updated Two Sum",
                1L,
                "HARD",
                "QUARTER_FINAL",
                "MUNICIPAL"
        );

        mockMvc.perform(put("/problems/{id}", problemId)
                        .param("userId", "999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    private Long createProblemAndReturnId(Long ownerId) throws Exception {
        Map<String, Object> requestBody = createProblemRequestBody("Two Sum", ownerId,
                "EASY", "QUALIFICATION", "SCHOOL");

        String response = mockMvc.perform(post("/problems")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);

        return jsonNode.get("id").asLong();
    }

    private Map<String, Object> createProblemRequestBody(
            String title,
            Long ownerId,
            String generalDifficulty,
            String icpcDifficulty,
            String schoolDifficulty) {

        return Map.of(
                "title", title,
                "ownerId", ownerId,
                "generalDifficulty", generalDifficulty,
                "icpcDifficulty", icpcDifficulty,
                "schoolDifficulty", schoolDifficulty
        );
    }
}