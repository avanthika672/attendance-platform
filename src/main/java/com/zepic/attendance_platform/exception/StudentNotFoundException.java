package com.zepic.attendance_platform.exception;

import org.springframework.http.HttpStatus;

public class StudentNotFoundException extends ApiException {

    public StudentNotFoundException() {

        super(
                "STUDENT_NOT_FOUND", "Student was not found",
                HttpStatus.NOT_FOUND
        );
    }
}
