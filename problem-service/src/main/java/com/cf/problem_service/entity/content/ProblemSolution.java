package com.cf.problem_service.entity.content;

import com.cf.problem_service.enums.ProgrammingLanguage;
import jakarta.persistence.*;

@Entity
@Table(name = "problem_solutions")
public class ProblemSolution extends ProblemContent {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgrammingLanguage language;
}
