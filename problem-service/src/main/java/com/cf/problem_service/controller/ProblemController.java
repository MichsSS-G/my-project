package com.cf.problem_service.controller;

import java.util.List;

import com.cf.problem_service.dto.ProblemPatchRequestDto;
import com.cf.problem_service.dto.ProblemCreateRequestDto;
import com.cf.problem_service.dto.ProblemResponseDto;
import com.cf.problem_service.dto.ProblemUpdateRequestDto;
import com.cf.problem_service.entity.Problem;
import com.cf.problem_service.service.ProblemService;
import com.cf.problem_service.mapper.ProblemMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problems")
public class ProblemController {

    private final ProblemService problemService;
    
    private final ProblemMapper problemMapper;

    public ProblemController(ProblemService problemService, ProblemMapper problemMapper) {
        this.problemService = problemService;
        this.problemMapper = problemMapper;
    }

    @GetMapping
    public List<ProblemResponseDto> getAllProblems(@RequestParam Long userId) {
        return problemService.getAllProblems(userId).stream().map(problemMapper::mapToDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProblemResponseDto createProblem(@Valid @RequestBody ProblemCreateRequestDto problemCreateRequestDto) {
        Problem created = problemService.createProblem(problemMapper.mapToProblem(problemCreateRequestDto));
        return problemMapper.mapToDto(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProblem(@PathVariable Long id, @RequestParam Long userId) {
        problemService.deleteProblem(id, userId);
    }

    @PutMapping("/{id}")
    public ProblemResponseDto updateProblem(@PathVariable Long id, @RequestParam Long userId, @Valid @RequestBody ProblemUpdateRequestDto dto) {
        Problem updatedProblem = problemService.updateProblem(id, userId, problemMapper.mapToProblem(dto));
        return problemMapper.mapToDto(updatedProblem);
    }

    @PatchMapping("/{id}")
    public ProblemResponseDto patchProblem(@PathVariable Long id, @RequestParam Long userId, @Valid @RequestBody ProblemPatchRequestDto dto) {
        Problem patched = problemService.patchProblem(id, userId, problemMapper.mapToProblem(dto));
        return problemMapper.mapToDto(patched);
    }

    @GetMapping("/{id}")
    public ProblemResponseDto getProblemById(@PathVariable Long id, @RequestParam Long userId) {
        return problemMapper.mapToDto(problemService.getProblemById(id, userId));
    }
}
