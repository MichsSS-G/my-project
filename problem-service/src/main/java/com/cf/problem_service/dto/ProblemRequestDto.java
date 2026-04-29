package com.cf.problem_service.dto;

import com.cf.problem_service.enums.GeneralDifficulty;
import com.cf.problem_service.enums.IcpcDifficulty;
import com.cf.problem_service.enums.SchoolDifficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProblemRequestDto {

    @NotBlank
    @Size(min = 3, max = 30, message = "Problem's name length should be between 3 and 30")
    private String title;

    @NotNull(message = "Owner id cannot be null")
    private Long ownerId;

    private IcpcDifficulty icpcDifficulty;

    private SchoolDifficulty schoolDifficulty;

    private GeneralDifficulty generalDifficulty;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOwnerId(Long id) {
        this.ownerId = id;
    }

    public void setIcpcDifficulty(IcpcDifficulty icpcDifficulty) {
        this.icpcDifficulty = icpcDifficulty;
    }

    public void setSchoolDifficulty(SchoolDifficulty schoolDifficulty) {
        this.schoolDifficulty = schoolDifficulty;
    }

    public void setGeneralDifficulty(GeneralDifficulty generalDifficulty) {
        this.generalDifficulty = generalDifficulty;
    }

    public String getTitle() {
        return title;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public IcpcDifficulty getIcpcDifficulty() {
        return icpcDifficulty;
    }

    public SchoolDifficulty getSchoolDifficulty() {
        return schoolDifficulty;
    }

    public GeneralDifficulty getGeneralDifficulty() {
        return generalDifficulty;
    }
}
