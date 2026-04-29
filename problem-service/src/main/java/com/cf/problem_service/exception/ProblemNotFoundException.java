package com.cf.problem_service.exception;

import com.cf.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ProblemNotFoundException extends BaseException {

    public ProblemNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
