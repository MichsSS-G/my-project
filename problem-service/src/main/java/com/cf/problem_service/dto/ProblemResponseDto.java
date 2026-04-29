package com.cf.problem_service.dto;

public class ProblemResponseDto {

    private final Long id;
    private final Long ownerId;

    private final String title;

    public ProblemResponseDto(Long id, Long ownerId, String title) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getTitle() {
        return title;
    }
}
