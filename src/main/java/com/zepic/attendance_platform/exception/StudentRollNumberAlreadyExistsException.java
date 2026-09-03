package com.zepic.attendance_platform.exception;

import org.springframework.http.HttpStatus;

public class StudentRollNumberAlreadyExistsException extends ApiException {
    public StudentRollNumberAlreadyExistsException () {
        super(
                "STUDENT_ROLL_NUMBER_ALREADY_EXISTS", "A student with this roll number already exists",
                HttpStatus.CONFLICT
        );

    }

}