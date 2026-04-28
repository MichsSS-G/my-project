package com.cf.problem_service.entity;

import com.cf.problem_service.enums.GeneralDifficulty;
import com.cf.problem_service.enums.IcpcDifficulty;
import com.cf.problem_service.enums.SchoolDifficulty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "problems")
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    @Column(nullable = false, length = 100)
    private String title;

    @Enumerated(EnumType.STRING)
    private SchoolDifficulty schoolDifficulty;

    @Enumerated(EnumType.STRING)
    private IcpcDifficulty icpcDifficulty;

    @Enumerated(EnumType.STRING)
    private GeneralDifficulty generalDifficulty;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSchoolDifficulty(SchoolDifficulty schoolDifficulty) {
        this.schoolDifficulty = schoolDifficulty;
    }

    public void setIcpcDifficulty(IcpcDifficulty icpcDifficulty) {
        this.icpcDifficulty = icpcDifficulty;
    }

    public void setGeneralDifficulty(GeneralDifficulty generalDifficulty) {
        this.generalDifficulty = generalDifficulty;
    }

    public SchoolDifficulty getSchoolDifficulty() {
        return schoolDifficulty;
    }

    public IcpcDifficulty getIcpcDifficulty() {
        return icpcDifficulty;
    }

    public GeneralDifficulty getGeneralDifficulty() {
        return generalDifficulty;
    }

    public String getTitle() {
        return title;
    }

    public Long getId() {
        return id;
    }

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
