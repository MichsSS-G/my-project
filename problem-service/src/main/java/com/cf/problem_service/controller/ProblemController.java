package com.cf.problem_service.controller;

import java.util.List;

import com.cf.problem_service.dto.ProblemPatchRequestDto;
import com.cf.problem_service.dto.ProblemRequestDto;
import com.cf.problem_service.dto.ProblemResponseDto;
import com.cf.problem_service.entity.Problem;
import com.cf.problem_service.service.ProblemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @GetMapping
    public List<ProblemResponseDto> getAllProblems(@RequestParam Long userId) {
        return problemService.getAllProblems(userId).stream().map(this::mapToDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProblemResponseDto createProblem(@Valid @RequestBody ProblemRequestDto problemRequestDto) {
        Problem created = problemService.createProblem(mapToProblem(problemRequestDto));
        return mapToDto(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProblem(@PathVariable Long id, @RequestParam Long userId) {
        problemService.deleteProblem(id, userId);
    }

    @PutMapping("/{id}")
    public ProblemResponseDto updateProblem(@PathVariable Long id, @RequestParam Long userId, @Valid @RequestBody ProblemRequestDto dto) {
        Problem updatedProblem = problemService.updateProblem(id, userId, mapToProblem(dto));
        return mapToDto(updatedProblem);
    }

    @PatchMapping("/{id}")
    public ProblemResponseDto patchProblem(@PathVariable Long id, @RequestParam Long userId, @Valid @RequestBody ProblemPatchRequestDto dto) {
        Problem patched = problemService.patchProblem(id, userId, mapToProblem(dto));
        return mapToDto(patched);
    }

    @GetMapping("/{id}")
    public ProblemResponseDto getProblemById(@PathVariable Long id, @RequestParam Long userId) {
        return mapToDto(problemService.getProblemById(id, userId));
    }

    private ProblemResponseDto mapToDto(Problem problem) {
        return new ProblemResponseDto(problem.getId(), problem.getOwnerId(), problem.getTitle());
    }

    private Problem mapToProblem(ProblemRequestDto dto) {
        Problem problem = new Problem();

        problem.setOwnerId(dto.getOwnerId());
        problem.setTitle(dto.getTitle());
        problem.setIcpcDifficulty(dto.getIcpcDifficulty());
        problem.setSchoolDifficulty(dto.getSchoolDifficulty());
        problem.setGeneralDifficulty(dto.getGeneralDifficulty());

        return problem;
    }

    private Problem mapToProblem(ProblemPatchRequestDto dto) {
        Problem problem = new Problem();

        problem.setTitle(dto.getTitle());
        problem.setIcpcDifficulty(dto.getIcpcDifficulty());
        problem.setSchoolDifficulty(dto.getSchoolDifficulty());
        problem.setGeneralDifficulty(dto.getGeneralDifficulty());

        return problem;
    }
}
