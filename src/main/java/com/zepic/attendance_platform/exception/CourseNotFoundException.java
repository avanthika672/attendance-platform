package com.zepic.attendance_platform.exception;

import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends ApiException {

    public CourseNotFoundException() {

        super(
                "COURSE_NOT_FOUND", "Course was not found",
                HttpStatus.NOT_FOUND
        );
    }
}