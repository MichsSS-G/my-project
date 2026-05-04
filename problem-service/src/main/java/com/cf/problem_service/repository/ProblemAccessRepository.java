package com.cf.problem_service.repository;

import com.cf.problem_service.access.ProblemAccess;
import com.cf.problem_service.entity.Problem;
import com.cf.problem_service.enums.Role;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemAccessRepository extends JpaRepository<ProblemAccess, Long> {

    Optional<ProblemAccess> findByProblemIdAndUserId(Long problemId, Long userId);

    boolean existsByProblemIdAndUserIdAndRole(Long problemId, Long userId, Role role);

    List<ProblemAccess> getProblemAccessesByProblemId(Long problemId);

    List<ProblemAccess> getProblemAccessesByUserId(Long userId);

    void deleteByProblemIdAndUserId(Long problemId, Long userId);

    List<ProblemAccess> findAllByUserId(Long userId);

    void deleteAllByProblemId(Long id);
}
