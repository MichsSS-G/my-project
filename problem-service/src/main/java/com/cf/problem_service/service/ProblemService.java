package com.cf.problem_service.service;

import com.cf.problem_service.access.ProblemAccess;
import com.cf.problem_service.entity.Problem;
import com.cf.problem_service.enums.Role;
import com.cf.problem_service.exception.AccessDeniedException;
import com.cf.problem_service.exception.ProblemNotFoundException;
import com.cf.problem_service.repository.ProblemAccessRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cf.problem_service.repository.ProblemRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProblemService {

    private final static String NO_ACCESS_MESSAGE = "You have no access to this problem";

    private final ProblemRepository repository;

    private final ProblemAccessRepository accessRepository;

    public ProblemService(ProblemRepository repository, ProblemAccessRepository accessRepository) {
        this.repository = repository;
        this.accessRepository = accessRepository;
    }

    private Problem getProblemByIdOrThrow(Long id) {
        Optional<Problem> optionalProblem = repository.findById(id);
        if (optionalProblem.isEmpty()) {
            throw new ProblemNotFoundException("Problem with id " + id + " doesn't exist");
        }
        return optionalProblem.get();
    }

    @Transactional
    public Problem createProblem(Problem problem) {
        Problem savedProblem = repository.save(problem);
        ProblemAccess problemAccess = new ProblemAccess();
        problemAccess.setProblemId(savedProblem.getId());
        problemAccess.setUserId(savedProblem.getOwnerId());
        problemAccess.setRole(Role.OWNER);
        accessRepository.save(problemAccess);
        return savedProblem;
    }

    public List<Problem> getAllProblems() {
        return repository.findAll();
    }

    public Problem getProblemById(Long id) {
        return getProblemByIdOrThrow(id);
    }

    @Transactional
    public void deleteProblem(Long id, Long userId) {
        Problem problem = getProblemByIdOrThrow(id);
        checkCanModify(id, userId);
        repository.delete(problem);
    }

    @Transactional
    public Problem updateProblem(Long id, Long userId, Problem problem) {
        Problem current = getProblemByIdOrThrow(id);
        checkCanModify(id, userId);

        current.setTitle(problem.getTitle());
        current.setIcpcDifficulty(problem.getIcpcDifficulty());
        current.setSchoolDifficulty(problem.getSchoolDifficulty());
        current.setGeneralDifficulty(problem.getGeneralDifficulty());

        return repository.save(current);
    }

    @Transactional
    public Problem patchProblem(Long id, Long userId, Problem problem) {
        Problem currentProblem = getProblemByIdOrThrow(id);
        checkCanModify(id, userId);

        if (problem.getTitle() != null) {
            currentProblem.setTitle(problem.getTitle());
        }

        if (problem.getGeneralDifficulty() != null) {
            currentProblem.setGeneralDifficulty(problem.getGeneralDifficulty());
        }

        if (problem.getSchoolDifficulty() != null) {
            currentProblem.setSchoolDifficulty(problem.getSchoolDifficulty());
        }

        if (problem.getIcpcDifficulty() != null) {
            currentProblem.setIcpcDifficulty(problem.getIcpcDifficulty());
        }

        return repository.save(currentProblem);
    }

    private ProblemAccess getAccessOrThrow(Long problemId, Long userId) {
        Optional<ProblemAccess> optionalProblemAccess = accessRepository.findByProblemIdAndUserId(problemId, userId);
        if (optionalProblemAccess.isPresent()) {
            return optionalProblemAccess.get();
        }
        throw new AccessDeniedException(NO_ACCESS_MESSAGE);
    }

    private void checkCanRead(Long problemId, Long userId) {
        ProblemAccess problemAccess = getAccessOrThrow(problemId, userId);
        if (problemAccess.getRole() == null) {
            throw new AccessDeniedException(NO_ACCESS_MESSAGE);
        }
    }

    private void checkCanModify(Long problemId, Long userId) {
        ProblemAccess problemAccess = getAccessOrThrow(problemId, userId);
        if (problemAccess.getRole() != Role.MODERATOR && problemAccess.getRole() != Role.OWNER) {
            throw new AccessDeniedException(NO_ACCESS_MESSAGE);
        }
    }

    private void checkIsOwner(Long problemId, Long userId) {
        ProblemAccess problemAccess = getAccessOrThrow(problemId, userId);
        if (problemAccess.getRole() != Role.OWNER) {
            throw new AccessDeniedException("You haven't got an owner role");
        }
    }

    @Transactional(readOnly = true)
    public Problem getProblemById(Long id, Long userId) {
        Problem problem = getProblemByIdOrThrow(id);
        checkCanRead(id, userId);
        return problem;
    }
}
