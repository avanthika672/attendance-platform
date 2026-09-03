package com.zepic.attendance_platform.exception;

import org.springframework.http.HttpStatus;

public class CollegeNotFoundException extends ApiException {

    public CollegeNotFoundException() {
        super(
                "COLLEGE_NOT_FOUND", "College not found",
                HttpStatus.NOT_FOUND
        );
    }
}