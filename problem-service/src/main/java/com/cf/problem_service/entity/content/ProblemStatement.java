package com.cf.problem_service.entity.content;

import com.cf.problem_service.enums.StatementFormat;
import jakarta.persistence.*;

@Entity
@Table(name = "problem_statements")
public class ProblemStatement extends ProblemContent {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long problemId;

    @Enumerated(EnumType.STRING)
    private StatementFormat statementFormat;


}
