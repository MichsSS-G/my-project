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

    @Transactional(readOnly = true)
    public List<Problem> getAllProblems(Long userId) {
        List<Long> problemIds = accessRepository.findAllByUserId(userId).stream().map(ProblemAccess::getProblemId).toList();

        if (problemIds.isEmpty()) {
            return List.of();
        }

        return repository.findAllByIdIn(problemIds);
    }

    @Transactional
    public void deleteProblem(Long id, Long userId) {
        Problem problem = getProblemByIdOrThrow(id);
        checkCanModify(id, userId);
        repository.delete(problem);
    }
    
    private Problem getProblemForModification(Long problemId, Long userId) {
        Problem problem = getProblemByIdOrThrow(problemId);
        checkCanModify(problemId, userId);
        return problem;
    }

    @Transactional
    public Problem updateProblem(Long problemId, Long userId, Problem source) {
        Problem current = getProblemForModification(problemId, userId);

        current.setTitle(source.getTitle());
        current.setGeneralDifficulty(source.getGeneralDifficulty());
        current.setSchoolDifficulty(source.getSchoolDifficulty());
        current.setIcpcDifficulty(source.getIcpcDifficulty());

        return repository.save(current);
    }

    @Transactional
    public Problem patchProblem(Long problemId, Long userId, Problem source) {
        Problem current = getProblemForModification(problemId, userId);

        if (source.getTitle() != null) {
            current.setTitle(source.getTitle());
        }

        if (source.getGeneralDifficulty() != null) {
            current.setGeneralDifficulty(source.getGeneralDifficulty());
        }

        if (source.getSchoolDifficulty() != null) {
            current.setSchoolDifficulty(source.getSchoolDifficulty());
        }

        if (source.getIcpcDifficulty() != null) {
            current.setIcpcDifficulty(source.getIcpcDifficulty());
        }

        return repository.save(current);
    }

    private ProblemAccess getAccessOrThrow(Long problemId, Long userId) {
        Optional<ProblemAccess> optionalProblemAccess = accessRepository.findByProblemIdAndUserId(problemId, userId);
        if (optionalProblemAccess.isPresent()) {
            return optionalProblemAccess.get();
        }
        throw new AccessDeniedException(NO_ACCESS_MESSAGE);
    }

    private void checkCanRead(Long problemId, Long userId) {
        getAccessOrThrow(problemId, userId);
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
