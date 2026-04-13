package entity.content;

import entity.enums.ProgrammingLanguage;
import jakarta.persistence.*;

@Entity
@Table(name = "problem_solutions")
public class ProblemSolution extends ProblemContent {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProgrammingLanguage language;
}
