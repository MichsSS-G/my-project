package com.cf.problem_service.dto;

import com.cf.problem_service.enums.GeneralDifficulty;
import com.cf.problem_service.enums.IcpcDifficulty;
import com.cf.problem_service.enums.SchoolDifficulty;

public class ProblemResponseDto {

    private final Long id;
    private final Long ownerId;

    private final String title;
    private final GeneralDifficulty generalDifficulty;
    private final IcpcDifficulty icpcDifficulty;
    private final SchoolDifficulty schoolDifficulty;

    public ProblemResponseDto(Long id,
                              Long ownerId,
                              String title,
                              GeneralDifficulty generalDifficulty,
                              IcpcDifficulty icpcDifficulty,
                              SchoolDifficulty schoolDifficulty) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.generalDifficulty = generalDifficulty;
        this.icpcDifficulty = icpcDifficulty;
        this.schoolDifficulty = schoolDifficulty;
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

    public GeneralDifficulty getGeneralDifficulty() {
        return generalDifficulty;
    }

    public IcpcDifficulty getIcpcDifficulty() {
        return icpcDifficulty;
    }

    public SchoolDifficulty getSchoolDifficulty() {
        return schoolDifficulty;
    }
}
