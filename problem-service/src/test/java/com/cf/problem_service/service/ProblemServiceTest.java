package com.cf.problem_service.service;

import com.cf.problem_service.access.ProblemAccess;
import com.cf.problem_service.entity.Problem;
import com.cf.problem_service.enums.GeneralDifficulty;
import com.cf.problem_service.enums.IcpcDifficulty;
import com.cf.problem_service.enums.Role;
import com.cf.problem_service.enums.SchoolDifficulty;
import com.cf.problem_service.exception.AccessDeniedException;
import com.cf.problem_service.exception.ProblemNotFoundException;
import com.cf.problem_service.repository.ProblemAccessRepository;
import com.cf.problem_service.repository.ProblemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProblemServiceTest {

    private static final String TITLE = "Sum";
    private static final String SECOND_TITLE = "Big Sum";
    private static final String OLD_TITLE = "Old title";
    private static final String NEW_TITLE = "New title";

    private static final Long OWNER_ID = 1L;
    private static final Long SECOND_OWNER_ID = 2L;
    private static final Long PROBLEM_ID = 25L;
    private static final Long SECOND_PROBLEM_ID = 26L;
    private static final Long UNKNOWN_USER_ID = 666L;

    private ProblemRepository problemRepository;
    private ProblemAccessRepository problemAccessRepository;
    private ProblemService problemService;

    @BeforeEach
    void setUp() {
        problemRepository = mock(ProblemRepository.class);
        problemAccessRepository = mock(ProblemAccessRepository.class);
        problemService = new ProblemService(problemRepository, problemAccessRepository);
    }

    private void setId(Problem problem, Long id) {
        try {
            Field field = Problem.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(problem, id);
        }
        catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Problem createProblem(Long id, Long ownerId, String title) {
        Problem problem = new Problem();

        setId(problem, id);
        problem.setOwnerId(ownerId);
        problem.setTitle(title);
        problem.setGeneralDifficulty(GeneralDifficulty.EASY);
        problem.setIcpcDifficulty(IcpcDifficulty.QUALIFICATION);
        problem.setSchoolDifficulty(SchoolDifficulty.SCHOOL);

        return problem;
    }

    private ProblemAccess createProblemAccess(Long problemId, Long userId, Role role) {
        ProblemAccess problemAccess = new ProblemAccess();

        problemAccess.setProblemId(problemId);
        problemAccess.setUserId(userId);
        problemAccess.setRole(role);

        return problemAccess;
    }

    @Test
    @DisplayName("create Problem should save problem and create OWNER access")
    void createProblemShouldSaveProblemAndCreateOwnerAccess() {
        Problem problem = createProblem(null, OWNER_ID, TITLE);
        Problem savedProblem = createProblem(PROBLEM_ID, OWNER_ID, TITLE);

        when(problemRepository.save(problem)).thenReturn(savedProblem);

        Problem result = problemService.createProblem(problem);

        assertEquals(PROBLEM_ID, result.getId());
        assertEquals(OWNER_ID, result.getOwnerId());

        verify(problemRepository).save(problem);

        ArgumentCaptor<ProblemAccess> accessArgumentCaptor = ArgumentCaptor.forClass(ProblemAccess.class);
        verify(problemAccessRepository).save(accessArgumentCaptor.capture());

        ProblemAccess createdAccess = accessArgumentCaptor.getValue();

        assertEquals(PROBLEM_ID, createdAccess.getProblemId());
        assertEquals(OWNER_ID, createdAccess.getUserId());
        assertEquals(Role.OWNER, createdAccess.getRole());
    }

    @Test
    @DisplayName("get problem by id should return problem when user has access")
    void getProblemByIdShouldReturnProblemWhenUserHasAccess() {
        Problem problem = createProblem(PROBLEM_ID, OWNER_ID, TITLE);
        ProblemAccess problemAccess = createProblemAccess(PROBLEM_ID, OWNER_ID, Role.READER);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(problem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, OWNER_ID)).thenReturn(Optional.of(problemAccess));

        Problem result = problemService.getProblemById(PROBLEM_ID, OWNER_ID);

        assertEquals(PROBLEM_ID, result.getId());
        assertEquals(TITLE, result.getTitle());

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, OWNER_ID);
    }

    @Test
    @DisplayName("get problem by id should throw ProblemNotFoundException when problem does not exist")
    void getProblemByIdShouldThrowProblemNotFoundExceptionWhenProblemDoesNotExist() {
        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.empty());

        assertThrows(ProblemNotFoundException.class, () -> problemService.getProblemById(PROBLEM_ID, OWNER_ID));

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository, never()).findByProblemIdAndUserId(PROBLEM_ID, OWNER_ID);
    }

    @Test
    @DisplayName("get problem by id should throw AccessDeniedException when user has no access")
    void getProblemByIdShouldThrowAccessDeniedExceptionWhenUserHasNoAccess() {
        Problem problem = createProblem(PROBLEM_ID, OWNER_ID, TITLE);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(problem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID)).thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class, () -> problemService.getProblemById(PROBLEM_ID, UNKNOWN_USER_ID));

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID);
    }

    @Test
    @DisplayName("patchProblem should update only provided fields")
    void patchProblemShouldUpdateOnlyProvidedFields() {
        Problem currentProblem = createProblem(PROBLEM_ID, OWNER_ID, OLD_TITLE);
        currentProblem.setGeneralDifficulty(GeneralDifficulty.EASY);
        currentProblem.setIcpcDifficulty(IcpcDifficulty.QUALIFICATION);
        currentProblem.setSchoolDifficulty(SchoolDifficulty.SCHOOL);

        Problem patchProblem = new Problem();
        patchProblem.setTitle(NEW_TITLE);
        patchProblem.setGeneralDifficulty(GeneralDifficulty.MEDIUM);

        ProblemAccess access = createProblemAccess(PROBLEM_ID, OWNER_ID, Role.OWNER);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(currentProblem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, OWNER_ID)).thenReturn(Optional.of(access));
        when(problemRepository.save(currentProblem)).thenReturn(currentProblem);

        var result = problemService.patchProblem(PROBLEM_ID, OWNER_ID, patchProblem);

        assertEquals(NEW_TITLE, result.getTitle());
        assertEquals(GeneralDifficulty.MEDIUM, result.getGeneralDifficulty());
        assertEquals(IcpcDifficulty.QUALIFICATION, result.getIcpcDifficulty());
        assertEquals(SchoolDifficulty.SCHOOL, result.getSchoolDifficulty());

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, OWNER_ID);
        verify(problemRepository).save(currentProblem);
    }

    @Test
    @DisplayName("update problem should update all editable fields when user can modify")
    void updateProblemShouldUpdateAllEditableFieldsWhenUserCanModify() {
        Problem currentProblem = createProblem(PROBLEM_ID, OWNER_ID, OLD_TITLE);
        Problem updateProblem = new Problem();
        updateProblem.setTitle(NEW_TITLE);
        updateProblem.setGeneralDifficulty(GeneralDifficulty.EXTREMELY_HARD);
        updateProblem.setIcpcDifficulty(IcpcDifficulty.WORLD_FINAL);
        updateProblem.setSchoolDifficulty(SchoolDifficulty.NATIONAL_FINAL);

        ProblemAccess access = createProblemAccess(PROBLEM_ID, UNKNOWN_USER_ID, Role.MODERATOR);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(currentProblem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID)).thenReturn(Optional.of(access));
        when(problemRepository.save(currentProblem)).thenReturn(currentProblem);

        Problem result = problemService.updateProblem(PROBLEM_ID, UNKNOWN_USER_ID, updateProblem);

        assertEquals(NEW_TITLE, result.getTitle());
        assertEquals(GeneralDifficulty.EXTREMELY_HARD, result.getGeneralDifficulty());
        assertEquals(IcpcDifficulty.WORLD_FINAL, result.getIcpcDifficulty());
        assertEquals(SchoolDifficulty.NATIONAL_FINAL, result.getSchoolDifficulty());

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID);
        verify(problemRepository).save(currentProblem);
    }

    @Test
    @DisplayName("update problem should throw AccessDeniedException when user is reader")
    void updateProblemShouldThrowAccessDeniedExceptionWhenUserIsReader() {
        Problem currentProblem = createProblem(PROBLEM_ID, OWNER_ID, OLD_TITLE);
        Problem updateProblem = new Problem();
        updateProblem.setTitle(NEW_TITLE);
        updateProblem.setGeneralDifficulty(GeneralDifficulty.EASY);
        updateProblem.setIcpcDifficulty(IcpcDifficulty.QUALIFICATION);
        updateProblem.setSchoolDifficulty(SchoolDifficulty.SCHOOL);

        ProblemAccess access = createProblemAccess(PROBLEM_ID, UNKNOWN_USER_ID, Role.READER);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(currentProblem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID)).thenReturn(Optional.of(access));

        assertThrows(AccessDeniedException.class, () -> problemService.updateProblem(PROBLEM_ID, UNKNOWN_USER_ID, updateProblem));

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID);
        verify(problemRepository, never()).save(currentProblem);
    }

    @Test
    @DisplayName("delete problem should delete problem and all accesses when user can modify")
    void deleteProblemShouldDeleteProblemAndAllAccessesWhenUserCanModify() {
        Problem problem = createProblem(PROBLEM_ID, OWNER_ID, TITLE);
        ProblemAccess access = createProblemAccess(PROBLEM_ID, OWNER_ID, Role.OWNER);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(problem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, OWNER_ID)).thenReturn(Optional.of(access));

        problemService.deleteProblem(PROBLEM_ID, OWNER_ID);

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, OWNER_ID);
        verify(problemAccessRepository).deleteAllByProblemId(PROBLEM_ID);
        verify(problemRepository).delete(problem);
    }

    @Test
    @DisplayName("patch problem should throw AccessDeniedException when user is reader")
    void patchProblemShouldThrowAccessDeniedExceptionWhenUserIsReader() {
        Problem currentProblem = createProblem(PROBLEM_ID, OWNER_ID, OLD_TITLE);

        Problem patchProblem = new Problem();
        patchProblem.setTitle(NEW_TITLE);

        ProblemAccess access = createProblemAccess(PROBLEM_ID, UNKNOWN_USER_ID, Role.READER);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(currentProblem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID)).thenReturn(Optional.of(access));

        assertThrows(AccessDeniedException.class, () -> problemService.patchProblem(PROBLEM_ID, UNKNOWN_USER_ID, patchProblem));

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID);
        verify(problemRepository, never()).save(currentProblem);
    }

    @Test
    @DisplayName("delete problem should throw AccessDeniedException when user is moderator")
    void deleteProblemShouldThrowAccessDeniedExceptionWhenUserIsModerator() {
        Problem problem = createProblem(PROBLEM_ID, OWNER_ID, TITLE);
        ProblemAccess access = createProblemAccess(PROBLEM_ID, UNKNOWN_USER_ID, Role.MODERATOR);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(problem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID)).thenReturn(Optional.of(access));

        assertThrows(AccessDeniedException.class, () -> problemService.deleteProblem(PROBLEM_ID, UNKNOWN_USER_ID));

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID);
        verify(problemAccessRepository, never()).deleteAllByProblemId(PROBLEM_ID);
        verify(problemRepository, never()).delete(problem);
    }

    @Test
    @DisplayName("delete problem should throw AccessDeniedException when user is reader")
    void deleteProblemShouldThrowAccessDeniedExceptionWhenUserIsReader() {
        Problem problem = createProblem(PROBLEM_ID, OWNER_ID, TITLE);
        ProblemAccess access = createProblemAccess(PROBLEM_ID, UNKNOWN_USER_ID, Role.READER);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(problem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID)).thenReturn(Optional.of(access));

        assertThrows(AccessDeniedException.class, () -> problemService.deleteProblem(PROBLEM_ID, UNKNOWN_USER_ID));

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID);
        verify(problemAccessRepository, never()).deleteAllByProblemId(PROBLEM_ID);
        verify(problemRepository, never()).delete(problem);
    }

    @Test
    @DisplayName("delete problem should throw AccessDeniedException when user has no access")
    void deleteProblemShouldThrowAccessDeniedExceptionWhenUserHasNoAccess() {
        Problem problem = createProblem(PROBLEM_ID, OWNER_ID, TITLE);

        when(problemRepository.findById(PROBLEM_ID)).thenReturn(Optional.of(problem));
        when(problemAccessRepository.findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID)).thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class, () -> problemService.deleteProblem(PROBLEM_ID, UNKNOWN_USER_ID));

        verify(problemRepository).findById(PROBLEM_ID);
        verify(problemAccessRepository).findByProblemIdAndUserId(PROBLEM_ID, UNKNOWN_USER_ID);
        verify(problemAccessRepository, never()).deleteAllByProblemId(PROBLEM_ID);
        verify(problemRepository, never()).delete(problem);
    }

    @Test
    @DisplayName("get all problems should return only problems available to user")
    void getAllProblemsShouldReturnOnlyProblemsAvailableToUser() {

        Problem firstProblem = createProblem(PROBLEM_ID, OWNER_ID, TITLE);
        Problem secondProblem = createProblem(SECOND_PROBLEM_ID, SECOND_OWNER_ID, SECOND_TITLE);

        ProblemAccess firstAccess = createProblemAccess(PROBLEM_ID, OWNER_ID, Role.OWNER);
        ProblemAccess secondAccess = createProblemAccess(SECOND_PROBLEM_ID, OWNER_ID, Role.READER);

        when(problemAccessRepository.findAllByUserId(OWNER_ID)).thenReturn(List.of(firstAccess, secondAccess));

        when(problemRepository.findAllByIdIn(List.of(PROBLEM_ID, SECOND_PROBLEM_ID))).thenReturn(List.of(firstProblem, secondProblem));

        List<Problem> result = problemService.getAllProblems(OWNER_ID);

        assertEquals(2, result.size());
        assertEquals(PROBLEM_ID, result.get(0).getId());
        assertEquals(SECOND_PROBLEM_ID, result.get(1).getId());

        verify(problemAccessRepository).findAllByUserId(OWNER_ID);
        verify(problemRepository).findAllByIdIn(List.of(PROBLEM_ID, SECOND_PROBLEM_ID));
    }

    @Test
    @DisplayName("get all problems should return empty list when user has no accesses")
    void getAllProblemsShouldReturnEmptyListWhenUserHasNoAccesses() {
        when(problemAccessRepository.findAllByUserId(UNKNOWN_USER_ID)).thenReturn(List.of());

        List<Problem> result = problemService.getAllProblems(UNKNOWN_USER_ID);

        assertTrue(result.isEmpty());

        verify(problemAccessRepository).findAllByUserId(UNKNOWN_USER_ID);
        verify(problemRepository, never()).findAllByIdIn(anyList());
    }
}
