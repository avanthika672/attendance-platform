package com.zepic.attendance_platform.exception;

import org.springframework.http.HttpStatus;

public class StudentEmailAlreadyExistsException extends ApiException {
    public StudentEmailAlreadyExistsException() {
        super(
                "STUDENT_EMAIL_ALREADY_EXISTS", "A student with this email already exists",
                HttpStatus.CONFLICT
        );
    }
}