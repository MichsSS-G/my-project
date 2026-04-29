package com.cf.problem_service.repository;

import com.cf.problem_service.access.ProblemAccess;
import com.cf.problem_service.entity.Problem;
import com.cf.problem_service.enums.Role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProblemAccessRepository extends JpaRepository<ProblemAccess, Long> {

    Optional<ProblemAccess> findByProblemIdAndUserId(Long problemId, Long userId);

    boolean existsByProblemIdAndUserIdAndRole(Long problemId, Long userId, Role role);

    List<ProblemAccess> getProblemAccessesByProblemId(Long problemId);

    List<ProblemAccess> getProblemAccessesByUserId(Long userId);

    void deleteByProblemIdAndUserId(Long problemId, Long userId);
}
