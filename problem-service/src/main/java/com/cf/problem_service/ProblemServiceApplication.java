package com.cf.problem_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.cf.problem_service", "com.cf.common"})
public class ProblemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemServiceApplication.class, args);
    }
}
