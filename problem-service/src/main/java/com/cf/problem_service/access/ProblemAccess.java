package com.cf.problem_service.access;

import com.cf.problem_service.enums.Role;
import jakarta.persistence.*;

@Entity
@Table(
        name = "problem_accesses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"problem_id", "user_id"})
)
public class ProblemAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "problem_id", nullable = false)
    private Long problemId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    public Long getId() {
        return id;
    }

    public Long getProblemId() {
        return problemId;
    }

    public Long getUserId() {
        return userId;
    }

    public Role getRole() {
        return role;
    }

    public void setProblemId(Long problemId) {
        this.problemId = problemId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
