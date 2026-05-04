package com.cf.problem_service.dto;

import com.cf.problem_service.enums.GeneralDifficulty;
import com.cf.problem_service.enums.IcpcDifficulty;
import com.cf.problem_service.enums.SchoolDifficulty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProblemUpdateRequestDto {

    @NotBlank
    @Size(min = 3, max = 30, message = "Problem's name length should be between 3 and 30")
    private String title;

    private GeneralDifficulty generalDifficulty;

    private IcpcDifficulty icpcDifficulty;

    private SchoolDifficulty schoolDifficulty;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGeneralDifficulty(GeneralDifficulty generalDifficulty) {
        this.generalDifficulty = generalDifficulty;
    }

    public void setIcpcDifficulty(IcpcDifficulty icpcDifficulty) {
        this.icpcDifficulty = icpcDifficulty;
    }

    public void setSchoolDifficulty(SchoolDifficulty schoolDifficulty) {
        this.schoolDifficulty = schoolDifficulty;
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
