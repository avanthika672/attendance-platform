package com.zepic.attendance_platform.exception;

public class CourseNotFoundException extends ApiException {

    public CourseNotFoundException() {
        super("COURSE_NOT_FOUND", "Course was not found");
    }
}