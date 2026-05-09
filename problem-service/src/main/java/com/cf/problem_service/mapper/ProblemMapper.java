package com.cf.problem_service.mapper;

import com.cf.problem_service.dto.ProblemCreateRequestDto;
import com.cf.problem_service.dto.ProblemPatchRequestDto;
import com.cf.problem_service.dto.ProblemResponseDto;
import com.cf.problem_service.dto.ProblemUpdateRequestDto;
import com.cf.problem_service.entity.Problem;
import org.springframework.stereotype.Component;

@Component
public class ProblemMapper {

    public ProblemResponseDto mapToDto(Problem problem) {
        return new ProblemResponseDto(problem.getId(), problem.getOwnerId(), problem.getTitle());
    }

    public Problem mapToProblem(ProblemCreateRequestDto dto) {
        var problem = new Problem();
        problem.setTitle(dto.getTitle());
        problem.setOwnerId(dto.getOwnerId());
        problem.setGeneralDifficulty(dto.getGeneralDifficulty());
        problem.setIcpcDifficulty(dto.getIcpcDifficulty());
        problem.setSchoolDifficulty(dto.getSchoolDifficulty());
        return problem;
    }

    public Problem mapToProblem(ProblemUpdateRequestDto dto) {
        var problem = new Problem();
        problem.setTitle(dto.getTitle());
        problem.setGeneralDifficulty(dto.getGeneralDifficulty());
        problem.setIcpcDifficulty(dto.getIcpcDifficulty());
        problem.setSchoolDifficulty(dto.getSchoolDifficulty());
        return problem;
    }

    public Problem mapToProblem(ProblemPatchRequestDto dto) {
        var problem = new Problem();
        if (dto.getTitle() != null) {
            problem.setTitle(dto.getTitle());
        }
        if (dto.getGeneralDifficulty() != null) {
            problem.setGeneralDifficulty(dto.getGeneralDifficulty());
        }
        if (dto.getIcpcDifficulty() != null) {
            problem.setIcpcDifficulty(dto.getIcpcDifficulty());
        }
        if (dto.getSchoolDifficulty() != null) {
            problem.setSchoolDifficulty(dto.getSchoolDifficulty());
        }
        return problem;
    }
}
