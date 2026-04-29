package com.cf.problem_service.service;

import com.cf.problem_service.entity.Problem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.cf.problem_service.repository.ProblemRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ProblemService {

    private final ProblemRepository repository;

    public ProblemService(ProblemRepository repository) {
        this.repository = repository;
    }

    private Problem getProblemByIdOrThrow(Long id) {
        Optional<Problem> optionalProblem = repository.findById(id);
        if (optionalProblem.isEmpty()) {
            throw new IllegalArgumentException("Problem with id " + id + " doesn't exist");
        }
        return optionalProblem.get();
    }

    @Transactional
    public Problem createProblem(Problem problem) {
        return repository.save(problem);
    }

    public List<Problem> getAllProblems() {
        return repository.findAll();
    }

    public Problem getProblemById(Long id) {
        return getProblemByIdOrThrow(id);
    }

    @Transactional
    public void deleteProblem(Long id) {
        Problem problem = getProblemByIdOrThrow(id);
        repository.delete(problem);
    }

    @Transactional
    public Problem updateProblem(Long id, Problem problem) {
        Problem current = getProblemByIdOrThrow(id);
        current.setTitle(problem.getTitle());
        current.setIcpcDifficulty(problem.getIcpcDifficulty());
        current.setSchoolDifficulty(problem.getSchoolDifficulty());
        current.setGeneralDifficulty(problem.getGeneralDifficulty());

        return repository.save(current);
    }

    @Transactional
    public Problem patchProblem(Long id, Problem problem) {
        Problem currentProblem = getProblemByIdOrThrow(id);

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
}
